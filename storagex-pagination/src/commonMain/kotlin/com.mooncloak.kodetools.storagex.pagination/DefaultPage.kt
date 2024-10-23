package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a page of information loaded from a paginated data source.
 *
 * @property [dataSourceId] A unique identifier value for the data source. This value is especially
 * important when you have numerous data sources that are being merged into a single source
 * (ex: using [PageCollection]), as it provides a way to load the next page of data from the
 * appropriate data source.
 *
 * @property [id] A unique identifier value for this [ResolvedPage] instance.
 *
 * @property [items] The [List] of returned items of type [Item].
 *
 * @property [info] The [PageInfo] associated with this [Page].
 *
 * @see [ResolvedPage]
 */
@ExperimentalPaginationAPI
@Serializable
public data class DefaultResolvedPage<Item> @PublishedApi internal constructor(
    @SerialName(value = "id") public override val id: String,
    @SerialName(value = "source_id") public override val dataSourceId: String? = null,
    @SerialName(value = "page_cursor") public override val pageCursor: Cursor? = null,
    @SerialName(value = "items") public override val items: List<Item> = emptyList(),
    @SerialName(value = "info") public override val info: PageInfo = PageInfo()
) : ResolvedPage<Item>

/**
 * Represents a page of information loaded from a paginated data source.
 *
 * @property [dataSourceId] A unique identifier value for the data source. This value is especially
 * important when you have numerous data sources that are being merged into a single source
 * (ex: using [PageCollection]), as it provides a way to load the next page of data from the
 * appropriate data source.
 *
 * @property [id] A unique identifier value for this [ResolvedPage] instance.
 *
 * @property [items] The [List] of returned items of type [Item].
 *
 * @property [info] The [PageInfo] associated with this [Page].
 *
 * @property [original] The original [PageRequest] associated with the request that resulted in
 * this page being returned.
 *
 * @property [actual] The actual [PageRequest] associated with the request that resulted in this
 * page being returned. This can be different from [original] if there was any formatting or
 * altering of data by the source.
 *
 * @see [ResolvedPage]
 */
@ExperimentalPaginationAPI
@Serializable
public data class DefaultResolvedPageWithRequestData<Request : Any, Filter : Any, Item> @PublishedApi internal constructor(
    @SerialName(value = "id") public override val id: String,
    @SerialName(value = "source_id") public override val dataSourceId: String? = null,
    @SerialName(value = "page_cursor") public override val pageCursor: Cursor? = null,
    @SerialName(value = "items") public override val items: List<Item> = emptyList(),
    @SerialName(value = "info") public override val info: PageInfo = PageInfo(),
    @SerialName(value = "original") public val original: PageRequest<Request, Filter>? = null,
    @SerialName(value = "actual") public val actual: PageRequest<Request, Filter>? = original
) : ResolvedPage<Item>

/**
 * Represents a default [PageCollection] instance.
 *
 * @see [PageCollection]
 */
@ExperimentalPaginationAPI
@Serializable
public data class DefaultPageCollection<Item> @PublishedApi internal constructor(
    @SerialName(value = "id") public override val id: String,
    @SerialName(value = "page_cursor") public override val pageCursor: Cursor,
    @SerialName(value = "source_id") public override val dataSourceId: String? = null,
    @SerialName(value = "pages") public override val pages: List<ResolvedPage<Item>> = emptyList()
) : PageCollection<Item>

/**
 * Represents a default [PagePlaceholder] instance.
 *
 * @see [PagePlaceholder]
 */
@ExperimentalPaginationAPI
public data class DefaultPagePlaceholder<Item> @PublishedApi internal constructor(
    public override val id: String,
    override val pageCursor: Cursor? = null,
    override val dataSourceId: String? = null,
    private val getter: suspend () -> ResolvedPage<Item>
) : PagePlaceholder<Item> {

    override suspend fun resolve(): ResolvedPage<Item> =
        getter.invoke()
}

/**
 * Creates a default [ResolvedPage] instance wrapping the provided values.
 *
 * @see [ResolvedPage]
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline operator fun <Item> ResolvedPage.Companion.invoke(
    id: String = Uuid.random().toHexString(),
    dataSourceId: String? = null,
    pageCursor: Cursor? = null,
    items: List<Item> = emptyList(),
    info: PageInfo = PageInfo()
): DefaultResolvedPage<Item> = DefaultResolvedPage(
    id = id,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor,
    items = items,
    info = info
)

/**
 * Creates a default [ResolvedPage] instance wrapping the provided values.
 *
 * @see [ResolvedPage]
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline operator fun <Request : Any, Filter : Any, Item> ResolvedPage.Companion.invoke(
    id: String = Uuid.random().toHexString(),
    dataSourceId: String? = null,
    pageCursor: Cursor? = null,
    items: List<Item> = emptyList(),
    info: PageInfo = PageInfo(),
    original: PageRequest<Request, Filter>? = null,
    actual: PageRequest<Request, Filter>? = original
): DefaultResolvedPageWithRequestData<Request, Filter, Item> = DefaultResolvedPageWithRequestData(
    id = id,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor,
    items = items,
    info = info,
    original = original,
    actual = actual
)

/**
 * Creates a default [PageCollection] instance wrapping the provided [pages].
 *
 * @see [PageCollection]
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline operator fun <Item> PageCollection.Companion.invoke(
    id: String = Uuid.random().toHexString(),
    pageCursor: Cursor,
    dataSourceId: String? = null,
    pages: List<ResolvedPage<Item>> = emptyList()
): DefaultPageCollection<Item> = DefaultPageCollection(
    id = id,
    pages = pages,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor
)

/**
 * Creates a default [PagePlaceholder] instance wrapping the provided [getter] value.
 *
 * @see [PagePlaceholder]
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline operator fun <Item> PagePlaceholder.Companion.invoke(
    id: String = Uuid.random().toHexString(),
    pageCursor: Cursor? = null,
    noinline getter: suspend () -> ResolvedPage<Item>
): DefaultPagePlaceholder<Item> = DefaultPagePlaceholder(
    id = id,
    pageCursor = pageCursor,
    getter = getter
)
