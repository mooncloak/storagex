package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a request to a paginated data source.
 *
 * @property [request] The query [Request] value provided to the page request. This may not be relevant to the
 * request and therefore may be `null`.
 *
 * @property [direction] The [Direction] requested relative to the [cursor].
 *
 * @property [cursor] The [Cursor] provided to the request.
 *
 * @property [count] The amount of items requested to be loaded.
 *
 * @property [sort] The [SortOptions] defining the order of the results of the pagination request.
 *
 * @property [filters] A model that filters the results of the pagination request.
 */
@Serializable
@SerialName(value = "filter")
public data class PageRequest<Request, Filter> @PublishedApi internal constructor(
    @SerialName(value = "request") public val request: Request,
    @SerialName(value = "direction") public val direction: Direction = Direction.After,
    @SerialName(value = "cursor") public val cursor: Cursor? = null,
    @SerialName(value = "count") public val count: UInt = DEFAULT_COUNT,
    @SerialName(value = "sort") public val sort: SortOptions? = null,
    @SerialName(value = "filters") public val filters: Filter? = null
) {

    public companion object {

        public const val DEFAULT_COUNT: UInt = 25u
    }
}
