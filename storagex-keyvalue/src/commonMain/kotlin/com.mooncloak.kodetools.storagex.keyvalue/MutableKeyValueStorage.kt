package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.serializer
import kotlin.coroutines.cancellation.CancellationException

public interface MutableKeyValueStorage<Key : Any> : KeyValueStorage<Key> {

    /**
     * Sets a value associated with the given key.
     *
     * This function is used to store a value in a persistent storage. It supports serialization using the provided
     * [serializer]. If the [value] is null, the entry associated with the [key] will be removed from the storage.
     *
     * @param [key] The key to associate with the value. Must be a non-empty string.
     *
     * @param [value] The value to store, or null to remove the entry.
     *
     * @param [serializer] The serializer used to serialize and deserialize the value.
     *
     * @param [Value] The type of the value being stored. Must be a non-null type.
     *
     * @throws [IllegalArgumentException] if the [key] is invalid.
     *
     * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     */
    @Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
    public suspend fun <Value : Any> set(
        key: Key,
        value: Value?,
        serializer: KSerializer<Value>
    )

    /**
     * Removes the value associated with the given [key].
     *
     * If a value is associated with the [key], it will be removed. If no value is associated with the [key], this
     * function does nothing.
     *
     * @param [key] The key associated with the value to be removed.
     *
     * @throws [IllegalArgumentException] if the [key] is invalid.
     *
     * @throws [CancellationException] If the suspending function was cancelled.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun remove(key: Key)

    /**
     * Clears all data associated with this key-value storage.
     *
     * This function should be used to reset the state or remove any stored information that is no longer needed. After
     * calling [clear], the context should be in a state equivalent to when it was first initialized.
     */
    public suspend fun clear()

    public companion object
}

/**
 * Sets a value associated with the given key.
 *
 * This function is used to store a value in a persistent storage. It supports serialization using the obtained
 * [serializer]. If the [value] is null, the entry associated with the [key] will be removed from the storage.
 *
 * This is a convenience function for invoking the [MutableKeyValueStorage.set] function with a serializer value
 * obtained via the [serializer] inline function.
 *
 * @param [key] The key to associate with the value. Must be a non-empty string.
 *
 * @param [value] The value to store, or null to remove the entry.
 *
 * @param [Value] The type of the value being stored. Must be a non-null type.
 *
 * @throws [IllegalArgumentException] if the [key] is invalid.
 *
 * @throws [SerializationException] If a serialization-related issue occurs during deserialization.
 *
 * @throws [CancellationException] If the suspending function was cancelled.
 *
 * @see [MutableKeyValueStorage.set]
 */
@Throws(IllegalArgumentException::class, SerializationException::class, CancellationException::class)
public suspend inline fun <reified Key : Any, reified Value : Any> MutableKeyValueStorage<Key>.set(
    key: Key,
    value: Value?
) {
    this.set(
        key = key,
        value = value,
        serializer = serializer()
    )
}
