package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a page of information loaded from a paginated data source.
 *
 * @property [items] The [List] of returned items of type [T].
 *
 * @property [info] The [PageInfo] associated with this [Page].
 *
 * @property [request] The [PageRequest] associated with the request that resulted in this page
 * being returned.
 */
@Serializable
public class Page<T> public constructor(
    @SerialName(value = "items") public val items: List<T> = emptyList(),
    @SerialName(value = "info") public val info: PageInfo = PageInfo(),
    @SerialName(value = "request") public val request: PageRequest = PageRequest(filterKey = null),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Page<*>) return false

        if (items != other.items) return false
        if (info != other.info) return false
        if (request != other.request) return false

        return request == other.request
    }

    override fun hashCode(): Int {
        var result = items.hashCode()
        result = 31 * result + info.hashCode()
        result = 31 * result + request.hashCode()
        return result
    }

    override fun toString(): String =
        "Page(items=$items, info=$info, request=$request)"

    public companion object
}
