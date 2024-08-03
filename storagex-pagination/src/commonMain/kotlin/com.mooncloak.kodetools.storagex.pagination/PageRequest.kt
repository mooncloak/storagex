package com.mooncloak.kodetools.storagex.pagination

import com.mooncloak.kodetools.storagex.pagination.PageRequest.Companion.DEFAULT_COUNT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat

/**
 * Represents a request to a paginated data source.
 *
 * @property [query] The query [String] provided to the request. This may not be relevant to the
 * request and therefore may be `null`.
 *
 * @property [direction] The [Direction] requested relative to the [cursor].
 *
 * @property [cursor] The [Cursor] provided to the request.
 *
 * @property [count] The amount of items requested to be loaded.
 *
 * @property [sort] The [SortOptions] defining the order of the results of the pagination request.
 */
@Serializable
public sealed interface PageRequest {

    public val query: String?
    public val direction: Direction
    public val cursor: Cursor?
    public val count: UInt
    public val sort: SortOptions?

    public companion object {

        public const val DEFAULT_COUNT: UInt = 25u
    }
}

/**
 * Constructs a [FilterPageRequest] instance with the provided [filter] value.
 */
public operator fun <Filter> PageRequest.Companion.invoke(
    query: String? = null,
    direction: Direction = Direction.After,
    cursor: Cursor? = null,
    count: UInt = DEFAULT_COUNT,
    sort: SortOptions? = null,
    filter: Filter?
): FilterPageRequest<Filter> = FilterPageRequest(
    query = query,
    direction = direction,
    cursor = cursor,
    count = count,
    sort = sort,
    filter = filter
)

/**
 * Constructs a [FilterKeyPageRequest] instance with the provided [filterKey] value.
 */
public operator fun PageRequest.Companion.invoke(
    query: String? = null,
    direction: Direction = Direction.After,
    cursor: Cursor? = null,
    count: UInt = DEFAULT_COUNT,
    sort: SortOptions? = null,
    filterKey: FilterKey? = null
): FilterKeyPageRequest = FilterKeyPageRequest(
    query = query,
    direction = direction,
    cursor = cursor,
    count = count,
    sort = sort,
    filterKey = filterKey
)

/**
 * Decodes and returns the [Filter] from this [PageRequest] instance, or `null` if there is no
 * associated [Filter] or [FilterKey], or it could not be decoded as the type [Filter].
 */
public inline fun <reified Filter> PageRequest.decodeFilter(format: StringFormat): Filter? =
    when (this) {
        is FilterKeyPageRequest -> this.filterKey?.decode<Filter>(format = format)
        is FilterPageRequest<*> -> this.filter as? Filter
    }

/**
 * Encodes this [PageRequest] into a [FilterKeyPageRequest]. If this [PageRequest] is already a
 * [FilterKeyPageRequest], then this value is simply returned. Otherwise, if this [PageRequest] is
 * a [FilterPageRequest], then the [FilterPageRequest.filter] value of type [Filter] is encoded to
 * a [FilterKey], via the [FilterKey.Companion.encode] function, and a new [FilterKeyPageRequest]
 * is constructed with the resulting [FilterKey] value.
 */
public inline fun <reified Filter> PageRequest.encodeFilter(format: StringFormat): FilterKeyPageRequest =
    when (this) {
        is FilterKeyPageRequest -> this
        is FilterPageRequest<*> -> {
            val filter = this.filter as? Filter

            val key = filter?.let { FilterKey.encode<Filter>(value = it, format = format) }

            PageRequest(
                query = this.query,
                direction = this.direction,
                cursor = this.cursor,
                count = this.count,
                sort = this.sort,
                filterKey = key
            )
        }
    }

/**
 * Represents a [PageRequest] that includes a [Filter] model.
 *
 * @property [filter] A model that filters the results of the pagination request.
 */
@Serializable
@SerialName(value = "filter")
public class FilterPageRequest<Filter> @PublishedApi internal constructor(
    @SerialName(value = "query") public override val query: String? = null,
    @SerialName(value = "direction") public override val direction: Direction = Direction.After,
    @SerialName(value = "cursor") public override val cursor: Cursor? = null,
    @SerialName(value = "count") public override val count: UInt = DEFAULT_COUNT,
    @SerialName(value = "sort") public override val sort: SortOptions? = null,
    @SerialName(value = "filter") public val filter: Filter? = null
) : PageRequest {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FilterPageRequest<*>) return false

        if (query != other.query) return false
        if (direction != other.direction) return false
        if (cursor != other.cursor) return false
        if (sort != other.sort) return false
        if (filter != other.filter) return false

        return count == other.count
    }

    override fun hashCode(): Int {
        var result = query?.hashCode() ?: 0
        result = 31 * result + direction.hashCode()
        result = 31 * result + (cursor?.hashCode() ?: 0)
        result = 31 * result + count.toInt()
        result = 31 * result + (sort?.hashCode() ?: 0)
        result = 31 * result + (filter?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "FilterPageRequest(query=$query, direction=$direction, cursor=$cursor, count=$count, sort=$sort, filter=$filter)"

    public companion object
}

/**
 * Represents a [PageRequest] that includes a [FilterKey] model.
 *
 * @property [filterKey] A [FilterKey] representing the encoded filters associated with this
 * pagination request.
 */
@Serializable
@SerialName(value = "filter_key")
public class FilterKeyPageRequest @PublishedApi internal constructor(
    @SerialName(value = "query") public override val query: String? = null,
    @SerialName(value = "direction") public override val direction: Direction = Direction.After,
    @SerialName(value = "cursor") public override val cursor: Cursor? = null,
    @SerialName(value = "count") public override val count: UInt = DEFAULT_COUNT,
    @SerialName(value = "sort") public override val sort: SortOptions? = null,
    @SerialName(value = "filter_key") public val filterKey: FilterKey? = null
) : PageRequest {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FilterKeyPageRequest) return false

        if (query != other.query) return false
        if (direction != other.direction) return false
        if (cursor != other.cursor) return false
        if (sort != other.sort) return false
        if (filterKey != other.filterKey) return false

        return count == other.count
    }

    override fun hashCode(): Int {
        var result = query?.hashCode() ?: 0
        result = 31 * result + direction.hashCode()
        result = 31 * result + (cursor?.hashCode() ?: 0)
        result = 31 * result + count.toInt()
        result = 31 * result + (sort?.hashCode() ?: 0)
        result = 31 * result + (filterKey?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "FilterKeyPageRequest(query=$query, direction=$direction, cursor=$cursor, count=$count, sort=$sort, filterKey=$filterKey)"

    public companion object
}
