package com.mooncloak.kodetools.storagex.keyvalue

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
     * Retrieves the amount of key-value pairs stored in the [KeyValueStorage].
     */
    public suspend fun count(): Int

    /**
     * Returns `true` if the [KeyValueStorage] is empty, `false` otherwise.
     */
    public suspend fun isEmpty(): Boolean =
        count() == 0

    /**
     * Checks if the key-value storage contains a value associated with the given key.
     *
     * This function asynchronously determines whether a specific key exists within the storage. It provides a way to
     * query the storage's contents without retrieving the actual value.
     *
     * @param [key] The key to check for existence within the storage.
     *
     * @throws [IllegalArgumentException] if the [key] is invalid.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return `true` if the storage contains a value associated with the specified key, `false` otherwise.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun contains(key: Key): Boolean

    /**
     * Retrieves a value associated with the given key from the storage.
     *
     * This function attempts to retrieve a stored value, deserialize it using the provided [deserializer], and return
     * the resulting object. If no value is found for the given key or if deserialization fails, it returns `null`.
     *
     * @param [key] The key associated with the desired value.
     *
     * @param [deserializer] The [KSerializer] responsible for deserializing the stored value into the expected type
     * [Value].
     *
     * @throws [IllegalArgumentException] if the [key] is invalid.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return The deserialized value if found, otherwise `null`.
     */
    @Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
    public suspend fun <Value : Any> get(
        key: Key,
        deserializer: KSerializer<Value>
    ): Value?

    /**
     * Retrieves a hot [Flow] of [Value]s for the provided [key] that emits whenever a change occurs to the underlying
     * value for that [key]. The resulting [Flow] starts with the current [Value] that is stored for the provided
     * [key].
     *
     * @param [key] The key to associate with the value. Must be a non-empty string.
     *
     * @param [deserializer] The [KSerializer] responsible for deserializing the stored value into the expected type
     * [Value].
     *
     * @throws [UnsupportedOperationException] if the implementation doesn't support this operation.
     *
     * @throws [IllegalArgumentException] if the [key] is invalid.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     *
     * @return A [Flow] of [Value]s that are emitted when changes occur.
     */
    @Throws(
        UnsupportedOperationException::class,
        IllegalArgumentException::class,
        SerializationException::class,
        CancellationException::class
    )
    public fun <Value : Any> flow(
        key: Key,
        deserializer: KSerializer<Value>
    ): Flow<Value?>

    public companion object
}

/**
 * Returns `true` if the [KeyValueStorage] is NOT empty, `false` otherwise. This is the inverse of
 * the [KeyValueStorage.isEmpty] function, provided for convenience.
 */
public suspend inline fun <Key : Any> KeyValueStorage<Key>.isNotEmpty(): Boolean =
    !isEmpty()

/**
 * Retrieves a value associated with the given key from the storage.
 *
 * This function attempts to retrieve a stored value, deserialize it, and return the resulting object. If no value is
 * found for the given key or if deserialization fails, it returns `null`.
 *
 * This is a convenience function for invoking the [KeyValueStorage.get] function with a deserializer value obtained
 * via the [serializer] inline function.
 *
 * @param [key] The key associated with the desired value.
 *
 * @throws [IllegalArgumentException] if the [key] is invalid.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @return The deserialized value if found, otherwise `null`.
 *
 * @see [KeyValueStorage.get]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public suspend inline fun <reified Key : Any, reified Value : Any> KeyValueStorage<Key>.get(key: Key): Value? =
    this.get(
        key = key,
        deserializer = serializer()
    )

/**
 * Retrieves a hot [Flow] of [Value]s for the provided [key] that emits whenever a change occurs to the underlying
 * value for that [key]. The resulting [Flow] starts with the current [Value] that is stored for the provided
 * [key].
 *
 * This is a convenience function for invoking the [KeyValueStorage.flow] function with a serializer value
 * obtained via the [serializer] inline function.
 *
 * @param [key] The key to associate with the value. Must be a non-empty string.
 *
 * @throws [IllegalArgumentException] if the [key] is invalid.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @return A [Flow] of [Value]s that are emitted when changes occur.
 *
 * @see [KeyValueStorage.flow]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public inline fun <reified Key : Any, reified Value : Any> KeyValueStorage<Key>.flow(
    key: Key
): Flow<Value?> = this.flow(
    key = key,
    deserializer = serializer()
)
