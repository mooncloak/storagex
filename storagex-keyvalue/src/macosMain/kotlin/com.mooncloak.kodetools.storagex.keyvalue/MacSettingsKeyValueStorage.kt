package com.mooncloak.kodetools.storagex.keyvalue

import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

public fun KeyValueStorage.Companion.Settings(
    settings: Settings,
    serializersModule: SerializersModule
): KeyValueStorage = SettingsKeyValueStorage(
    settings = settings,
    serializersModule = serializersModule
)

internal class SettingsKeyValueStorage internal constructor(
    private val settings: Settings,
    private val serializersModule: SerializersModule
) : KeyValueStorage {

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Long = settings.size.toLong()

    override suspend fun contains(key: String): Boolean =
        settings.contains(key)

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun <T : Any> get(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): T {
        val decoder = SettingsDecoder(
            settings = settings,
            key = key,
            serializersModule = serializersModule
        )

        return deserializer.deserializeOrElse(decoder, null)
            ?: throw NoSuchElementException("No value stored with key = $key")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun <T : Any> set(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?
    ) {
        mutex.withLock {
            if (value == null) {
                settings.remove(key = key)
            } else {
                val encoder = SettingsEncoder(
                    settings = settings,
                    key = key,
                    serializersModule = serializersModule
                )

                serializer.serialize(encoder, value)
            }
        }
    }

    override suspend fun remove(key: String) =
        mutex.withLock {
            settings.remove(key = key)
        }

    override suspend fun clear() =
        mutex.withLock {
            settings.clear()
        }
}

// The following is code is adapted from the Multi-platform Settings library.

@ExperimentalSerializationApi
private class SettingsEncoder(
    private val settings: Settings,
    key: String,
    public override val serializersModule: SerializersModule
) : AbstractEncoder() {

    // Stack of keys to track what we're encoding next
    private val keyStack = ArrayDeque<String>().apply { add(key) }
    private fun getKey() = keyStack.joinToString(".")

    // Depth increases with beginStructure() and decreases with endStructure(). Subtly different from keyStack size.
    // This is important so we can tell whether the last key on the stack refers to the current parent or a sibling.
    private var depth = 0

    public override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        if (keyStack.size > depth) {
            keyStack.removeLast()
        }
        keyStack.add(descriptor.getElementName(index))
        return true
    }


    public override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        depth++
        return super.beginStructure(descriptor)
    }

    public override fun endStructure(descriptor: SerialDescriptor) {
        depth--
        keyStack.removeLast()
    }

    public override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder {
        settings.putInt("${getKey()}.size", collectionSize)
        return super.beginCollection(descriptor, collectionSize)
    }

    public override fun encodeNotNullMark() = settings.putBoolean("${getKey()}?", true)
    public override fun encodeNull() {
        settings.remove(getKey())
        settings.putBoolean("${getKey()}?", false)
    }

    public override fun encodeBoolean(value: Boolean) = settings.putBoolean(getKey(), value)
    public override fun encodeByte(value: Byte) = settings.putInt(getKey(), value.toInt())
    public override fun encodeChar(value: Char) = settings.putInt(getKey(), value.code)
    public override fun encodeDouble(value: Double) = settings.putDouble(getKey(), value)
    public override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) =
        settings.putInt(getKey(), index)

    public override fun encodeFloat(value: Float) = settings.putFloat(getKey(), value)
    public override fun encodeInt(value: Int) = settings.putInt(getKey(), value)
    public override fun encodeLong(value: Long) = settings.putLong(getKey(), value)
    public override fun encodeShort(value: Short) = settings.putInt(getKey(), value.toInt())
    public override fun encodeString(value: String) = settings.putString(getKey(), value)
}

@ExperimentalSerializationApi
private class SettingsDecoder(
    private val settings: Settings,
    private val key: String,
    public override val serializersModule: SerializersModule
) : AbstractDecoder() {

    // Stacks of keys and indices so we can track index at arbitrary levels to know what we're decoding next
    private val keyStack = ArrayDeque<String>().apply { add(key) }
    private val indexStack = ArrayDeque<Int>().apply { add(0) }
    private fun getKey(): String = keyStack.joinToString(".")

    // Depth increases with beginStructure() and decreases with endStructure(). Subtly different from stack sizes.
    // This is important so we can tell whether the last items on the stack refer to the current parent or a sibling.
    private var depth = 0


    public override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (keyStack.size > depth) {
            keyStack.removeLast()
            indexStack.removeLast()
        }

        // Can usually ask descriptor for a size, except for collections
        val size = when (descriptor.kind) {
            StructureKind.LIST -> decodeCollectionSize(descriptor)
            StructureKind.MAP -> 2 * decodeCollectionSize(descriptor) // Maps look like lists [k1, v1, k2, v2, ...]
            else -> descriptor.elementsCount
        }

        return getNextIndex(descriptor, size)
    }

    private tailrec fun getNextIndex(descriptor: SerialDescriptor, size: Int): Int {
        val index = indexStack.removeLast()
        indexStack.addLast(index + 1)

        return when {
            index >= size -> CompositeDecoder.DECODE_DONE
            isMissingAndOptional(descriptor, index) -> getNextIndex(descriptor, size)
            else -> {
                keyStack.add(descriptor.getElementName(index))
                indexStack.add(0)
                index
            }
        }
    }

    private inline fun isMissingAndOptional(descriptor: SerialDescriptor, index: Int): Boolean {
        val key = "${getKey()}.${descriptor.getElementName(index)}"
        // Descriptor shows key is optional, key is not present, and nullability doesn't indicate key should be present
        return descriptor.isElementOptional(index) && key !in settings && settings.getBooleanOrNull(
            "$key?"
        ) != true
    }


    public override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        depth++
        return super.beginStructure(descriptor)
    }

    public override fun endStructure(descriptor: SerialDescriptor) {
        depth--
        keyStack.removeLast()
        indexStack.removeLast()
        if (keyStack.isEmpty()) {
            // We've reached the end of everything, so reset for potential decoder reuse
            keyStack.add(key)
            indexStack.add(0)
        }
    }

    public override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
        settings.getIntOrNull("${getKey()}.size") ?: deserializationError()

    public override fun decodeNotNullMark(): Boolean =
        settings.getBooleanOrNull("${getKey()}?") ?: deserializationError()

    public override fun decodeNull(): Nothing? = null

    // Unfortunately the only way we can interrupt serialization if data is missing is to throw here and catch elsewhere
    public override fun decodeBoolean(): Boolean =
        settings.getBooleanOrNull(getKey()) ?: deserializationError()

    public override fun decodeByte(): Byte =
        settings.getIntOrNull(getKey())?.toByte() ?: deserializationError()

    public override fun decodeChar(): Char =
        settings.getIntOrNull(getKey())?.toChar() ?: deserializationError()

    public override fun decodeDouble(): Double =
        settings.getDoubleOrNull(getKey()) ?: deserializationError()

    public override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        settings.getIntOrNull(getKey()) ?: deserializationError()

    public override fun decodeFloat(): Float =
        settings.getFloatOrNull(getKey()) ?: deserializationError()

    public override fun decodeInt(): Int = settings.getIntOrNull(getKey()) ?: deserializationError()
    public override fun decodeLong(): Long =
        settings.getLongOrNull(getKey()) ?: deserializationError()

    public override fun decodeShort(): Short =
        settings.getIntOrNull(getKey())?.toShort() ?: deserializationError()

    public override fun decodeString(): String =
        settings.getStringOrNull(getKey()) ?: deserializationError()

    // Hook to reset state after we throw during deserializationError()
    internal fun reset() {
        keyStack.clear()
        indexStack.clear()
        depth = 0
        keyStack.add(key)
        indexStack.add(0)
    }
}

// (Ab)uses Decoder machinery to enumerate all keys related to a serialized value, so they can be removed
@ExperimentalSerializationApi
private class SettingsRemover(
    private val settings: Settings,
    private val key: String,
    public override val serializersModule: SerializersModule
) : AbstractDecoder() {

    private val keys = mutableListOf<String>()
    fun removeKeys() {
        for (key in keys) {
            settings.remove(key)
        }
    }

    // Stacks of keys and indices so we can track index at arbitrary levels to know what we're decoding next
    private val keyStack = ArrayDeque<String>().apply { add(key) }
    private val indexStack = ArrayDeque<Int>().apply { add(0) }
    private fun getKey(): String = keyStack.joinToString(".")

    // Depth increases with beginStructure() and decreases with endStructure(). Subtly different from stack sizes.
    // This is important so we can tell whether the last items on the stack refer to the current parent or a sibling.
    private var depth = 0


    public override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (keyStack.size > depth) {
            keyStack.removeLast()
            indexStack.removeLast()
        }

        // Can usually ask descriptor for a size, except for collections
        val size = when (descriptor.kind) {
            StructureKind.LIST -> decodeCollectionSize(descriptor)
            StructureKind.MAP -> 2 * decodeCollectionSize(descriptor) // Maps look like lists [k1, v1, k2, v2, ...]
            else -> descriptor.elementsCount
        }

        return getNextIndex(descriptor, size)
    }

    private tailrec fun getNextIndex(descriptor: SerialDescriptor, size: Int): Int {
        val index = indexStack.removeLast()
        indexStack.addLast(index + 1)

        return when {
            index >= size -> CompositeDecoder.DECODE_DONE
            isMissingAndOptional(descriptor, index) -> getNextIndex(descriptor, size)
            else -> {
                keyStack.add(descriptor.getElementName(index))
                indexStack.add(0)
                index
            }
        }
    }

    private inline fun isMissingAndOptional(descriptor: SerialDescriptor, index: Int): Boolean {
        val key = "${getKey()}.${descriptor.getElementName(index)}"
        // Descriptor shows key is optional, key is not present, and nullability doesn't indicate key should be present
        val output =
            descriptor.isElementOptional(index) && key !in settings && settings.getBooleanOrNull("$key?") != true
        keys.add(key)
        keys.add("$key?")
        return output
    }


    public override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        depth++
        return super.beginStructure(descriptor)
    }

    public override fun endStructure(descriptor: SerialDescriptor) {
        depth--
        keyStack.removeLast()
        indexStack.removeLast()
        if (keyStack.isEmpty()) {
            // We've reached the end of everything, so reset for potential decoder reuse
            keyStack.add(key)
            indexStack.add(0)
        }
    }

    public override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        val output = settings.getInt("${getKey()}.size", 0)
        keys.add("${getKey()}.size")
        return output
    }

    public override fun decodeNotNullMark(): Boolean {
        val output = settings.getBoolean("${getKey()}?", false)
        keys.add("${getKey()}?")
        return output
    }

    public override fun decodeNull(): Nothing? = null

    public override fun decodeBoolean(): Boolean {
        keys.add(getKey())
        return false
    }

    public override fun decodeByte(): Byte {
        keys.add(getKey())
        return 0
    }

    public override fun decodeChar(): Char {
        keys.add(getKey())
        return '0'
    }

    public override fun decodeDouble(): Double {
        keys.add(getKey())
        return 0.0
    }

    public override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        keys.add(getKey())
        return 0
    }

    public override fun decodeFloat(): Float {
        keys.add(getKey())
        return 0f
    }

    public override fun decodeInt(): Int {
        keys.add(getKey())
        return 0
    }

    public override fun decodeLong(): Long {
        keys.add(getKey())
        return 0
    }

    public override fun decodeShort(): Short {
        keys.add(getKey())
        return 0
    }

    public override fun decodeString(): String {
        keys.add(getKey())
        return ""
    }
}

private class DeserializationException : IllegalStateException()

private inline fun deserializationError(): Nothing = throw DeserializationException()

@ExperimentalSerializationApi
private inline fun <V, T : V> DeserializationStrategy<T>.deserializeOrElse(
    decoder: SettingsDecoder,
    defaultValue: V
): V =
    try {
        deserialize(decoder)
    } catch (_: DeserializationException) {
        decoder.reset()
        defaultValue
    }
