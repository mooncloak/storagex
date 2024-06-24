package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlin.reflect.KClass

/**
 * Creates an instance of the [KeyValueStorage] interface where the underlying values are stored in
 * an in-memory [Map], using the provided [format] to serialize and deserialize the values for
 * storage.
 *
 * @param [format] The [StringFormat] that is used to serialize and deserialize the values to and
 * from [String]s for storing the values.
 *
 * @return [KeyValueStorage]
 */
@Suppress("FunctionName")
public fun KeyValueStorage.Companion.InMemory(
    format: StringFormat
): KeyValueStorage = InMemoryKeyValueStorage(
    format = format
)

internal class InMemoryKeyValueStorage internal constructor(
    private val format: StringFormat
) : KeyValueStorage {

    private val map = mutableMapOf<String, String>()

    private val mutex = Mutex(locked = false)

    private val changes = MutableStateFlow<Pair<String, String?>?>(null)

    override suspend fun count(): Long = map.size.toLong()

    override suspend fun contains(key: String): Boolean = map.contains(key)

    override suspend fun <T : Any> get(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): T {
        val value = map[key] ?: throw NoSuchElementException("No entry found with key = $key.")

        return format.decodeFromString(deserializer = deserializer, string = value)
    }

    override suspend fun <T : Any> set(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?
    ) {
        mutex.withLock {
            if (value == null) {
                map.remove(key)

                changes.value = key to null
            } else {
                val stringValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                map[key] = stringValue

                changes.value = key to stringValue
            }
        }
    }

    override fun <T : Any> changes(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): Flow<T?> =
        changes.filterNotNull()
            .filter { pair -> pair.first == key }
            .map { pair ->
                pair.second?.let { value ->
                    format.decodeFromString(
                        deserializer = deserializer,
                        string = value
                    )
                }
            }

    override suspend fun remove(key: String) {
        mutex.withLock {
            map.remove(key = key)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            map.clear()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemoryKeyValueStorage) return false

        if (format != other.format) return false
        if (map != other.map) return false
        if (mutex != other.mutex) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + map.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String =
        "InMemoryKeyValueStorage(format=$format, map=$map, mutex=$mutex)"
}
