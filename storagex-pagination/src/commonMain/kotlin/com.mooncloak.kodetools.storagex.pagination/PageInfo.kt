package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents information associated with a paginated data source request.
 *
 * @property [hasPrevious] Whether there is a previous page that can be loaded. `true` if there is
 * a previous page that can be loaded, `false` if there is not a previous page, `null` if it is
 * unknown.
 *
 * @property [hasNext] Whether there is a next page that can be loaded. `true` if there is a next
 * page that can be loaded, `false` if there is not a next page, `null` if it is unknown.
 *
 * @property [firstCursor] The [Cursor] representing the first item in the returned list of items.
 *
 * @property [lastCursor] The [Cursor] representing the last item in the returned list of items.
 *
 * @property [totalCount] The total amount of items that can be queried and paginated through, or
 * `null` if the value is unknown.
 *
 * @property [returnedCount] The amount of items returned in this page. This is provided as a
 * convenience and should always match the amount of items in the [Page.items] list.
 */
@Serializable
public class PageInfo public constructor(
    @SerialName(value = "has_previous") public val hasPrevious: Boolean? = null,
    @SerialName(value = "has_next") public val hasNext: Boolean? = null,
    @SerialName(value = "first_cursor") public val firstCursor: Cursor? = null,
    @SerialName(value = "last_cursor") public val lastCursor: Cursor? = null,
    @SerialName(value = "count") public val returnedCount: UInt? = null,
    @SerialName(value = "total") public val totalCount: UInt? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PageInfo) return false

        if (hasPrevious != other.hasPrevious) return false
        if (hasNext != other.hasNext) return false
        if (firstCursor != other.firstCursor) return false
        if (lastCursor != other.lastCursor) return false
        if (returnedCount != other.returnedCount) return false

        return totalCount == other.totalCount
    }

    override fun hashCode(): Int {
        var result = hasPrevious?.hashCode() ?: 0
        result = 31 * result + (hasNext?.hashCode() ?: 0)
        result = 31 * result + (firstCursor?.hashCode() ?: 0)
        result = 31 * result + (lastCursor?.hashCode() ?: 0)
        result = 31 * result + (returnedCount?.toInt() ?: 0)
        result = 31 * result + (totalCount?.toInt() ?: 0)
        return result
    }

    override fun toString(): String =
        "PageInfo(hasPrevious=$hasPrevious, hasNext=$hasNext, firstCursor=$firstCursor, lastCursor=$lastCursor, returnedCount=$returnedCount, totalCount=$totalCount)"

    public companion object
}
