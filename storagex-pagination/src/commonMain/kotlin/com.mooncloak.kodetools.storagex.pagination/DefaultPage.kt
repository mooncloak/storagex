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
 * @property [original] The original [PageRequest] associated with the request that resulted in this page
 * being returned.
 *
 * @property [actual] The actual [PageRequest] associated with the request that resulted in this page
 * being returned. This can be different from [original] if there was any formatting or altering of data by the source.
 */
@ExperimentalPaginationAPI
@Serializable
@SerialName(value = "resolved")
public data class DefaultResolvedPage<Request, Filter, Type> public constructor(
    @SerialName(value = "source_id") public override val pageSourceId: String,
    @SerialName(value = "items") public override val items: List<Type> = emptyList(),
    @SerialName(value = "info") public override val info: PageInfo = PageInfo(),
    @SerialName(value = "original") public val original: PageRequest<Request, Filter>? = null,
    @SerialName(value = "actual") public val actual: PageRequest<Request, Filter>? = original
) : ResolvedPage<Type>

@ExperimentalPaginationAPI
@Serializable
@SerialName(value = "collection")
public data class DefaultPageCollection<Type> public constructor(
    @SerialName(value = "pages") override val pages: List<ResolvedPage<Type>>
) : PageCollection<Type>

/**
 * Creates a default [ResolvedPage] instance wrapping the provided values.
 */
@ExperimentalPaginationAPI
public inline operator fun <Request, Filter, Type> ResolvedPage.Companion.invoke(
    pageSourceId: String,
    items: List<Type> = emptyList(),
    info: PageInfo = PageInfo(),
    original: PageRequest<Request, Filter>? = null,
    actual: PageRequest<Request, Filter>? = original
): DefaultResolvedPage<Request, Filter, Type> = DefaultResolvedPage(
    pageSourceId = pageSourceId,
    items = items,
    info = info,
    original = original,
    actual = actual
)

/**
 * Creates a default [PageCollection] instance wrapping the provided [pages].
 */
@ExperimentalPaginationAPI
public inline operator fun <Type> PageCollection.Companion.invoke(
    pages: List<ResolvedPage<Type>> = emptyList()
): DefaultPageCollection<Type> = DefaultPageCollection(
    pages = pages
)
