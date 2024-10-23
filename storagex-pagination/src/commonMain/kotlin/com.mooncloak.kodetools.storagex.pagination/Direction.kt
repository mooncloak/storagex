package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the direction to paginate information relative to a [Cursor] from a data source.
 */
@ExperimentalPaginationAPI
@Serializable
public enum class Direction(
    public val serialName: String
) {

    /**
     * Paginate before a provided [Cursor].
     */
    @SerialName(value = "before")
    Before(serialName = "before"),

    /**
     * Paginate after a provided [Cursor].
     */
    @SerialName(value = "after")
    After(serialName = "after");

    public companion object {

        public operator fun get(value: String): Direction? =
            Direction.entries.firstOrNull { it.serialName.equals(value, ignoreCase = true) }
    }
}
