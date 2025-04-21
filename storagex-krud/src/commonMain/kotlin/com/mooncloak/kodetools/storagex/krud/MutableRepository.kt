package com.mooncloak.kodetools.storagex.krud

import kotlin.coroutines.cancellation.CancellationException

/**
 * A [Repository] that allows for mutable operations on stored values.
 *
 * This interface extends [Repository] and provides methods for inserting, updating, removing, and clearing data,
 * amongst other mutable operations.
 */
public interface MutableRepository<ID : Any, Value : Any> : Repository<ID, Value> {

    /**
     * Inserts the provided value with its corresponding [id] into the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [value] The function that returns the value to insert.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
     * match or an item already exists containing that [id].
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually inserted.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun insert(
        id: ID,
        value: () -> Value
    ): Value

    /**
     * Updates the value with the provided [id] with the updated value in the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [update] The function that is invoked with the current value and returns the newly updated value.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the updated [Value] does not
     * match.
     *
     * @throws [NoSuchElementException] If no item with the provided [id] exists.
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The updated [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually updated.
     */
    @Throws(IllegalArgumentException::class, NoSuchElementException::class, CancellationException::class)
    public suspend fun update(
        id: ID,
        update: (current: Value) -> Value
    ): Value

    /**
     * Inserts or updates the provided value with its corresponding [id] into the underlying storage.
     *
     * @param [id] The unique identifier of the value being inserted.
     *
     * @param [insert] The function that is invoked to retrieve the value to be inserted.
     *
     * @param [update] The function that is invoked to update the value that already exists.
     *
     * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
     * match or an item already exists containing that [id].
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     *
     * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
     * so this returned item might be different than the provided one, but the returned one represents the value that
     * is actually inserted.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun upsert(
        id: ID,
        insert: () -> Value,
        update: (current: Value) -> Value
    ): Value {
        val current = this.getOrNull(id = id)

        return if (current == null) {
            this.insert(
                id = id,
                value = insert
            )
        } else {
            this.update(
                id = id,
                update = update
            )
        }
    }

    /**
     * Removes the item containing the provided [id] from the underlying storage.
     *
     * @throws [IllegalArgumentException] if the provided [id] is invalid.
     *
     * @throws [CancellationException] If the coroutine is cancelled.
     */
    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun remove(id: ID)

    /**
     * Removes all items from the underlying storage.
     */
    public suspend fun clear()

    public companion object
}

/**
 * Inserts the provided value with its corresponding [id] into the underlying storage.
 *
 * @param [value] The function that returns the value to insert.
 *
 * @throws [IllegalArgumentException] if the provided [id] and the identifier of the constructed [Value] does not
 * match or an item already exists containing that [id].
 *
 * @throws [CancellationException] If the coroutine is cancelled.
 *
 * @return The insert [Value]. Note that the implementation may perform additional logic and formatting of values,
 * so this returned item might be different than the provided one, but the returned one represents the value that
 * is actually inserted.
 */
@Throws(IllegalArgumentException::class, CancellationException::class)
public suspend fun <ID : Any, Value : Entity<ID>> MutableRepository<ID, Value>.insert(
    value: Value
): Value = this.insert(
    id = value.id,
    value = { value }
)
