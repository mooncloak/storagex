package com.mooncloak.kodetools.storagex.keyvalue

/**
 * Represents a key-value property that can be modified.
 *
 * This interface extends [KeyValueProperty] and provides methods to set and remove the associated value.
 *
 * @param Value The type of the value associated with the property. Must be non-nullable.
 */
public interface MutableKeyValueProperty<Value : Any> : KeyValueProperty<Value> {

    /**
     * Sets the current value.
     *
     * This function allows updating the stored value asynchronously.
     *
     * @param value The new value to set. Can be null.
     */
    public suspend fun set(value: Value?)

    /**
     * Removes the current resource or object associated with this instance.
     */
    public suspend fun remove()

    public companion object
}

/**
 * A convenience function for updating the current value if it is already present.
 */
public suspend fun <Value : Any> MutableKeyValueProperty<Value>.update(block: (current: Value) -> Value) {
    val current = this.get()

    if (current != null) {
        val updated = block.invoke(current)

        this.set(value = updated)
    }
}
