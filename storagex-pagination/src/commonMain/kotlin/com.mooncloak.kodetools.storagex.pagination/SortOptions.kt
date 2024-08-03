package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public class SortOptions public constructor(
    @SerialName(value = "type") public val type: SortType,
    @SerialName(value = "order") public val order: SortOrder? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SortOptions) return false

        if (type != other.type) return false

        return order == other.order
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (order?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String = "SortOptions(type=$type, order=$order)"

    public companion object
}
