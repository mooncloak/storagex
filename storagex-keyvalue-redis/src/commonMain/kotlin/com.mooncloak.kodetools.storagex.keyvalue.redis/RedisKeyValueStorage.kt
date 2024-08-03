package com.mooncloak.kodetools.storagex.keyvalue.redis

import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.time.Duration

public interface RedisKeyValueStorage : MutableKeyValueStorage<String> {

    /**
     * Sets the value for the provided [key] to be equal to the provided [value], using the
     * provided [serializer] to convert the model of type [T] to its internal representation.
     *
     * @param [key] The [String] key value that uniquely identifies the data that is being set.
     * This same value can be used to retrieve the data later.
     *
     * @param [serializer] The [SerializationStrategy] that is used to convert the data into its
     * internal representation. This value must correspond to the [DeserializationStrategy] that is
     * used when retrieving the data, otherwise an exception will be thrown.
     *
     * @param [value] The value to set for the [key]. This value can be `null` to clear the cached
     * value.
     */
    public suspend fun <T : Any> put(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?,
        expiresIn: Duration
    )
}

/**
 * A convenience function for invoking the [KeyValueStorage.put] function with a
 * [SerializationStrategy] obtained via the [serializer] function.
 *
 * @see [MutableKeyValueStorage.put]
 */
public suspend inline fun <reified T : Any> RedisKeyValueStorage.put(
    key: String,
    value: T?,
    expiresIn: Duration
): Unit = put(
    key = key,
    serializer = serializer<T>(),
    kClass = T::class,
    value = value,
    expiresIn = expiresIn
)
