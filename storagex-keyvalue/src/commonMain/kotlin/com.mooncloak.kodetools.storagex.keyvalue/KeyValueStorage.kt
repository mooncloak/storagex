package com.mooncloak.kodetools.storagex.keyvalue

import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.*
import kotlin.reflect.KClass

/**
 * Represents a generic storage of data for key-value pairs.
 */
public interface KeyValueStorage {

    /**
     * Retrieves the amount of items stored in the underlying storage.
     */
    public suspend fun count(): Long

    /**
     * Determines if the entry with the provided [key] exists in the underlying storage.
     *
     * @return `true` if the entry with the provided [key] exists, `false` otherwise.
     */
    public suspend fun contains(key: String): Boolean

    /**
     * Retrieves the value for the provided [key], using the provided [deserializer] to convert the value from
     * its internal representation to the model of type [T], or throws [NoSuchElementException] if there is no value
     * for the provided [key] or it has expired.
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being retrieved.
     * @param [deserializer] The [DeserializationStrategy] that is used to convert the internal representation of the
     * data into the model of type [T]. If the wrong [deserializer] is used, an exception will be thrown.
     *
     * @throws [NoSuchElementException] If there is no element in the underlying storage with the provided [key].
     *
     * @return [T] The cached value, or throws [NoSuchElementException] if there is none, or it expired.
     */
    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun <T : Any> get(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): T

    /**
     * Sets the value for the provided [key] to be equal to the provided [value], using the provided [serializer] to
     * convert the model of type [T] to its internal representation.
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being set. This same value can be
     * used to retrieve the data later.
     * @param [serializer] The [SerializationStrategy] that is used to convert the data into its internal
     * representation. This value must correspond to the [DeserializationStrategy] that is used when retrieving the
     * data, otherwise an exception will be thrown.
     * @param [value] The value to set for the [key]. This value can be `null` to clear the cached value.
     */
    public suspend fun <T : Any> set(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?
    )

    /**
     * Retrieves a [Flow] of value changes for the provided [key]. Any time the value changes for
     * the [key], the new value will be emitted on the returned [Flow] instance.
     *
     * Note that implementations may not emit changes that occur out-of-band (changes made outside
     * of this component). It is up to the implementation if it wants to (and can) support those
     * changes.
     */
    public fun <T : Any> changes(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): Flow<T?> {
        throw UnsupportedOperationException("KeyValueStorage.changes function is not supported by this implementation.")
    }

    /**
     * Removes the cache item with the provided [key].
     */
    public suspend fun remove(key: String)

    /**
     * Clears all the stored data from this cache.
     */
    public suspend fun clear()

    /**
     * Closes the connection to the underlying data structure and clears any system resources.
     */
    public suspend fun close() {}

    public companion object
}

/**
 * Retrieves the value for the provided [key], using the provided [deserializer] to convert the value from
 * its internal representation to the model of type [T], or `null` if there is no cached value for the provided
 * [key] or it has expired.
 *
 * @param [key] The [String] key value that uniquely identifies the data that is being retrieved.
 * @param [deserializer] The [DeserializationStrategy] that is used to convert the internal representation of the
 * data into the model of type [T]. If the wrong [deserializer] is used, an exception will be thrown.
 *
 * @return [T] The cached value, or `null` if there is none, or it expired.
 */
public suspend fun <T : Any> KeyValueStorage.getOrNull(
    key: String,
    deserializer: KSerializer<T>,
    kClass: KClass<T>
): T? = try {
    get(
        key = key,
        deserializer = deserializer,
        kClass = kClass
    )
} catch (_: NoSuchElementException) {
    null
}

/**
 * A convenience function for invoking the [KeyValueStorage.get] function with a [DeserializationStrategy] obtained via
 * the [serializer] function.
 *
 * @see [KeyValueStorage.get]
 */
public suspend inline fun <reified T : Any> KeyValueStorage.get(key: String): T =
    get(
        key = key,
        deserializer = serializer<T>(),
        kClass = T::class
    )

/**
 * Retrieves the value for the provided [key], using the provided deserializer to convert the value from
 * its internal representation to the model of type [T], or `null` if there is no cached value for the provided
 * [key] or it has expired.
 *
 * @param [key] The [String] key value that uniquely identifies the data that is being retrieved.
 *
 * @return [T] The cached value, or `null` if there is none, or it expired.
 */
public suspend inline fun <reified T : Any> KeyValueStorage.getOrNull(
    key: String
): T? = try {
    get(
        key = key,
        deserializer = serializer<T>(),
        kClass = T::class
    )
} catch (_: NoSuchElementException) {
    null
}

/**
 * A convenience function for invoking the [KeyValueStorage.set] function with a [SerializationStrategy] obtained via
 * the [serializer] function.
 *
 * @see [KeyValueStorage.set]
 */
public suspend inline fun <reified T : Any> KeyValueStorage.set(key: String, value: T?): Unit =
    set(
        key = key,
        serializer = serializer<T>(),
        kClass = T::class,
        value = value
    )
