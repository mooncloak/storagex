package com.mooncloak.kodetools.storagex.keyvalue

import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage.StoredValue
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage.InputEntry
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage.InputValue
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.*

/**
 * Represents a generic storage of data for key-value pairs. This abstraction is used, instead of a
 * [Map] or similar interface, because each of the functions are suspending, allowing
 * implementations to have any data source and for them to use a [Mutex] to guarantee concurrency
 * safety.
 *
 * > [!Note]
 * > Implementations of this interface SHOULD guarantee to be concurrency-safe.
 */
public interface KeyValueStorage<Key : Any> {

    /**
     * The [SerialFormat] that is used to serialize and deserialize the values for the underlying
     * storage.
     */
    public val format: SerialFormat

    /**
     * Retrieves the amount of key-value pairs stored in the [KeyValueStorage].
     */
    public suspend fun size(): Long

    /**
     * Returns `true` if the [KeyValueStorage] is empty, `false` otherwise.
     */
    public suspend fun isEmpty(): Boolean =
        size() == 0L

    /**
     * Returns `true` if the [KeyValueStorage] contains the specified key, `false` otherwise.
     */
    public suspend fun containsKey(key: Key): Boolean =
        getValueOrNull(key = key) != null

    /**
     * Retrieves the [StoredValue] for the provided [key], or throws [NoSuchElementException] if
     * there is no value for the provided [key].
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being
     * retrieved.
     *
     * @throws [NoSuchElementException] If there is no element in the underlying storage with the
     * provided [key].
     *
     * @return The [StoredValue] associated with the provided [key], or throws
     * [NoSuchElementException] if there is none.
     */
    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getValue(key: Key): StoredValue<*>

    /**
     * Retrieves the value for the provided [key], using the provided [deserializer] to convert the
     * value from its internal representation to the model of type [T], or throws
     * [NoSuchElementException] if there is no value for the provided [key].
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being
     * retrieved.
     *
     * @param [deserializer] The [DeserializationStrategy] that is used to convert the internal
     * representation of the data into the model of type [T]. If the wrong [deserializer] is used,
     * an exception will be thrown.
     *
     * @throws [NoSuchElementException] If there is no element in the underlying storage with the
     * provided [key].
     *
     * @return [T] The value, or throws [NoSuchElementException] if there is none.
     */
    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun <T> get(
        key: Key,
        deserializer: DeserializationStrategy<T>
    ): T {
        val storedValue = getValue(key = key)

        return storedValue.value(deserializer = deserializer)
    }

    /**
     * Retrieves a [Flow] of value changes for the provided [key]. Any time the value changes for
     * the [key], the new value will be emitted on the returned [Flow] instance.
     *
     * @param [key] The [Key] whose value changes are to be observed.
     *
     * @throws [UnsupportedOperationException] if the implementation does not support this
     * functionality.
     *
     * @return A [Flow] of [StoredValue]s associated with the provided [key] and that are emitted
     * every time the value associated with the provided [key] changes.
     *
     * @see [KeyValueStorage.changesOrNull]
     */
    @Throws(UnsupportedOperationException::class)
    public fun changes(key: Key): Flow<StoredValue<*>?> {
        throw UnsupportedOperationException("KeyValueStorage.changes function is not supported by this implementation.")
    }

    /**
     * Returns a [Set] of all [Key]s in this [KeyValueStorage].
     */
    public suspend fun keys(): Set<Key> =
        entries().map { it.key }.toSet()

    /**
     * Returns a [Collection] of all [StoredValue]s in this [KeyValueStorage]. Note that the
     * returned [Collection] may return duplicate values.
     */
    public suspend fun values(): Collection<StoredValue<*>> =
        entries().map { it.storedValue }

    /**
     * Returns a [Set] of all [Entry] items in this [KeyValueStorage].
     */
    public suspend fun entries(): Set<Entry<Key, *>>

    /**
     * Closes the connection to the underlying data structure and clears any system resources.
     */
    public suspend fun close() {}

    public interface StoredValue<RawValue> {

        public val rawValue: RawValue
        public val format: SerialFormat

        public fun <Value> value(
            deserializer: DeserializationStrategy<Value>
        ): Value

        public companion object
    }

    public interface Entry<Key : Any, RawValue> {

        public val key: Key

        public val storedValue: StoredValue<RawValue>

        public operator fun component1(): Key = key

        public operator fun component2(): StoredValue<RawValue> = storedValue

        public companion object
    }

    public companion object
}

public interface MutableKeyValueStorage<Key : Any> : KeyValueStorage<Key> {

    /**
     * Sets the value for the provided [key] to be equal to the provided [value], using the
     * provided [serializer] to convert the model of type [Value] to its internal representation.
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being set.
     * This same value can be used to retrieve the data later.
     *
     * @param [serializer] The [SerializationStrategy] that is used to convert the data into its
     * internal representation. This value must correspond to the [DeserializationStrategy] that is
     * used when retrieving the data, otherwise an exception will be thrown.
     *
     * @param [value] The value to set for the [key]. This value can be `null` to clear the value.
     *
     * @return Returns the previous [StoredValue] associated with the [key], or `null` if the key
     * was not present in the [KeyValueStorage].
     */
    public suspend fun <Value : Any> put(
        key: Key,
        serializer: SerializationStrategy<Value>,
        value: Value?
    ): StoredValue<*>?

    /**
     * Puts all the provided [entries] into this [MutableKeyValueStorage].
     */
    public suspend fun <Value> putAll(entries: Collection<InputEntry<Key, Value>>)

    /**
     * Removes the entry with the provided [key].
     *
     * @return Returns the previous [StoredValue] associated with the [key], or `null` if the key
     * was not present in the [KeyValueStorage].
     */
    public suspend fun remove(key: Key): StoredValue<*>?

    /**
     * Clears all the stored data from this storage.
     */
    public suspend fun clear()

    public interface InputValue<Value> {

        public val value: Value
        public val serializer: SerializationStrategy<Value>

        public companion object
    }

    public interface InputEntry<Key : Any, Value> {

        public val key: Key
        public val inputValue: InputValue<Value>

        public operator fun component1(): Key = key

        public operator fun component2(): InputValue<Value> = inputValue

        public companion object
    }

    public companion object
}

public inline fun <reified Value> StoredValue<*>.value(): Value =
    value(deserializer = this.format.serializersModule.serializer())

/**
 * Returns `true` if the [KeyValueStorage] is NOT empty, `false` otherwise. This is the inverse of
 * the [KeyValueStorage.isEmpty] function, provided for convenience.
 */
public suspend fun <Key : Any> KeyValueStorage<Key>.isNotEmpty(): Boolean =
    !isEmpty()

/**
 * Retrieves the [StoredValue] for the provided [key], or `null` if there is no value for the
 * provided [key].
 *
 * @param [key] The [String] key value that uniquely identifies the data that is being
 * retrieved.
 *
 * @return The [StoredValue] associated with the provided [key], or `null` if there is none.
 */
public suspend fun <Key : Any> KeyValueStorage<Key>.getValueOrNull(key: Key): StoredValue<*>? =
    try {
        getValue(key = key)
    } catch (_: NoSuchElementException) {
        null
    }

/**
 * Retrieves the value for the provided [key], using the provided [deserializer] to convert the
 * value from its internal representation to the model of type [Value], or `null` if there is no value
 * for the provided [key] or it has expired.
 *
 * @param [key] The [String] key value that uniquely identifies the data that is being retrieved.
 *
 * @param [deserializer] The [DeserializationStrategy] that is used to convert the internal
 * representation of the data into the model of type [Value]. If the wrong [deserializer] is used, an
 * exception will be thrown.
 *
 * @return [Value] The cached value, or `null` if there is none, or it expired.
 */
public suspend inline fun <Key : Any, reified Value> KeyValueStorage<Key>.getOrNull(
    key: Key,
    deserializer: KSerializer<Value> = this.format.serializersModule.serializer()
): Value? = try {
    get(
        key = key,
        deserializer = deserializer
    )
} catch (_: NoSuchElementException) {
    null
}

/**
 * A convenience function for invoking the [KeyValueStorage.get] function with a
 * [DeserializationStrategy] obtained via the [serializer] function.
 *
 * @see [KeyValueStorage.get]
 */
public suspend inline fun <Key : Any, reified Value> KeyValueStorage<Key>.get(key: Key): Value =
    get(
        key = key,
        deserializer = this.format.serializersModule.serializer()
    )

/**
 * Retrieves a [Flow] of value changes for the provided [key]. Any time the value changes for
 * the [key], the new value will be emitted on the returned [Flow] instance.
 *
 * @param [key] The [Key] whose value changes are to be observed.
 *
 * @return A [Flow] of [StoredValue]s associated with the provided [key] and that are emitted
 * every time the value associated with the provided [key] changes, or `null` if this operation is
 * not supported by the implementation of this [KeyValueStorage] instance.
 *
 * @see [KeyValueStorage.changes]
 */
@Throws(UnsupportedOperationException::class)
public fun <Key : Any> KeyValueStorage<Key>.changesOrNull(key: Key): Flow<StoredValue<*>?>? =
    try {
        changes(key = key)
    } catch (_: UnsupportedOperationException) {
        null
    }

/**
 * Sets the value for the provided [key] to be equal to the provided [value], using the
 * provided [serializer] to convert the model of type [Value] to its internal representation. This
 * is a convenience function for invoking the [MutableKeyValueStorage.put] function with a
 * [SerializationStrategy] obtained via the [serializer] function.
 *
 * @param [key] The [String] key value that uniquely identifies the data that is being set.
 * This same value can be used to retrieve the data later.
 *
 * @param [value] The value to set for the [key]. This value can be `null` to clear the value.
 *
 * @return Returns the previous [StoredValue] associated with the [key], or `null` if the key
 * was not present in the [KeyValueStorage].
 *
 * @see [MutableKeyValueStorage.put]
 */
public suspend inline fun <Key : Any, reified Value : Any> MutableKeyValueStorage<Key>.put(
    key: Key,
    value: Value?
): StoredValue<*>? = put(
    key = key,
    serializer = this.format.serializersModule.serializer<Value>(),
    value = value
)

/**
 * Puts all the provided [entries] into this [MutableKeyValueStorage].
 */
public suspend fun <Key : Any, Value> MutableKeyValueStorage<Key>.putAll(vararg entries: InputEntry<Key, Value>): Unit =
    putAll(entries = entries.toList())

/**
 * Puts all the entries from the provided [map] into this [MutableKeyValueStorage].
 */
public suspend fun <Key : Any, Value> MutableKeyValueStorage<Key>.putAll(map: Map<Key, InputValue<Value>>): Unit =
    putAll(entries = map.entries.map { InputEntry(key = it.key, inputValue = it.value) })

/**
 * Puts all the provided [pairs] into this [MutableKeyValueStorage].
 */
public suspend fun <Key : Any, Value> MutableKeyValueStorage<Key>.putAll(vararg pairs: Pair<Key, InputValue<Value>>): Unit =
    putAll(entries = pairs.map { InputEntry(key = it.first, inputValue = it.second) })

/**
 * Puts all the provided [sequence] into this [MutableKeyValueStorage].
 */
public suspend fun <Key : Any, Value> MutableKeyValueStorage<Key>.putAll(sequence: Sequence<InputEntry<Key, Value>>): Unit =
    putAll(entries = sequence.toList())

/**
 * Creates a new [StoredValue] instance with the provided values.
 */
public operator fun StoredValue.Companion.invoke(
    rawValue: String,
    format: StringFormat
): StoredValue<String> = StringFormatStoredValue(
    rawValue = rawValue,
    format = format
)

/**
 * Creates a new [StoredValue] instance with the provided values.
 */
public operator fun <Value> StoredValue.Companion.invoke(
    rawValue: Value,
    format: SerialFormat
): StoredValue<Value> = NotSerializedStoredValue(
    rawValue = rawValue,
    format = format
)

/**
 * Creates a new [KeyValueStorage.Entry] instance with the provided values.
 */
public operator fun <Key : Any, Value> KeyValueStorage.Entry.Companion.invoke(
    key: Key,
    storedValue: StoredValue<Value>
): KeyValueStorage.Entry<Key, Value> = DefaultFormatStoredEntry(
    key = key,
    storedValue = storedValue
)

/**
 * Creates a new [KeyValueStorage.Entry] instance with the provided values.
 */
public operator fun <Key : Any> KeyValueStorage.Entry.Companion.invoke(
    key: Key,
    rawValue: String,
    format: StringFormat
): KeyValueStorage.Entry<Key, String> = DefaultFormatStoredEntry(
    key = key,
    storedValue = StoredValue.invoke(
        rawValue = rawValue,
        format = format
    )
)

/**
 * Creates a new [KeyValueStorage.Entry] instance with the provided values.
 */
public operator fun <Key : Any, Value> KeyValueStorage.Entry.Companion.invoke(
    key: Key,
    rawValue: Value,
    format: SerialFormat
): KeyValueStorage.Entry<Key, Value> = DefaultFormatStoredEntry(
    key = key,
    storedValue = StoredValue.invoke(
        rawValue = rawValue,
        format = format
    )
)

/**
 * Creates a new [InputValue] instance with the provided values.
 */
public operator fun <Value> InputValue.Companion.invoke(
    value: Value,
    serializer: SerializationStrategy<Value>
): InputValue<Value> = DefaultInputValue(
    value = value,
    serializer = serializer
)

/**
 * Creates a new [InputEntry] instance with the provided values.
 */
public operator fun <Key : Any, Value> InputEntry.Companion.invoke(
    key: Key,
    inputValue: InputValue<Value>
): InputEntry<Key, Value> = DefaultInputEntry(
    key = key,
    inputValue = inputValue
)

/**
 * Creates a new [InputEntry] instance with the provided values.
 */
public operator fun <Key : Any, Value> InputEntry.Companion.invoke(
    key: Key,
    value: Value,
    serializer: SerializationStrategy<Value>
): InputEntry<Key, Value> = DefaultInputEntry(
    key = key,
    inputValue = InputValue.invoke(
        value = value,
        serializer = serializer
    )
)

internal data class NotSerializedStoredValue<Value> internal constructor(
    override val rawValue: Value,
    override val format: SerialFormat
) : StoredValue<Value> {

    @Suppress("UNCHECKED_CAST")
    override fun <Value> value(deserializer: DeserializationStrategy<Value>): Value =
        rawValue as Value
}

internal data class StringFormatStoredValue internal constructor(
    override val rawValue: String,
    override val format: StringFormat
) : StoredValue<String> {

    override fun <Value> value(deserializer: DeserializationStrategy<Value>): Value =
        format.decodeFromString(
            deserializer = deserializer,
            string = rawValue
        )
}

internal data class DefaultFormatStoredEntry<Key : Any, Value> internal constructor(
    override val key: Key,
    override val storedValue: StoredValue<Value>
) : KeyValueStorage.Entry<Key, Value>

internal data class DefaultInputValue<Value> internal constructor(
    override val value: Value,
    override val serializer: SerializationStrategy<Value>
) : InputValue<Value>

internal data class DefaultInputEntry<Key : Any, Value> internal constructor(
    override val key: Key,
    override val inputValue: InputValue<Value>
) : InputEntry<Key, Value>
