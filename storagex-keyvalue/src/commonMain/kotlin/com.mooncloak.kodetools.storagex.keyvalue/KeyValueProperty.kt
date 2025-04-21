package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.flow.Flow

/**
 * Represents a property that holds a value associated with a key.
 *
 * This interface provides a way to retrieve the stored value asynchronously.
 *
 * @param Value The type of the value held by this property.
 */
public interface KeyValueProperty<Value : Any> {

    /**
     * Retrieves a value.
     *
     * This function attempts to retrieve a value.
     *
     * @return The retrieved value if it exists, or `null` if no value is available.
     */
    public suspend fun get(): Value?

    /**
     * Retrieves a [Flow] of the underlying value changes.
     */
    public fun flow(): Flow<Value?>

    public companion object
}
