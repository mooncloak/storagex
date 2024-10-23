package com.mooncloak.kodetools.storagex.pagination

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a result that can be returned from the [PagedDataSource.load] function.
 */
@ExperimentalPaginationAPI
public sealed interface PageLoadResult<Item> {

    public companion object
}

/**
 * A convenience function for accessing the [InvalidResult] type.
 */
@ExperimentalPaginationAPI
public inline fun PageLoadResult.Companion.invalid(): InvalidResult = InvalidResult

/**
 * A convenience function for creating an [ErrorResult] instance.
 */
@ExperimentalPaginationAPI
public inline fun <Item> PageLoadResult.Companion.error(
    cause: Throwable
): ErrorResult<Item> = ErrorResult(cause = cause)

/**
 * A convenience function for creating an empty [DefaultResolvedPage] instance.
 */
@ExperimentalPaginationAPI
public inline fun <Item> PageLoadResult.Companion.emptyPage(
    dataSourceId: String? = null,
    pageCursor: Cursor? = null,
    id: String = "EmptyPage",
    info: PageInfo = PageInfo()
): DefaultResolvedPage<Item> = ResolvedPage.invoke(
    dataSourceId = dataSourceId,
    pageCursor = pageCursor,
    id = id,
    items = emptyList(),
    info = info
)

/**
 * A convenience function for creating a [DefaultResolvedPage] instance.
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline fun <Item> PageLoadResult.Companion.page(
    id: String = Uuid.random().toHexString(),
    dataSourceId: String? = null,
    pageCursor: Cursor? = null,
    info: PageInfo = PageInfo(),
    items: List<Item> = emptyList()
): DefaultResolvedPage<Item> = ResolvedPage.invoke(
    id = id,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor,
    info = info,
    items = items
)

/**
 * A convenience function for creating a [DefaultResolvedPageWithRequestData] instance.
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline fun <Data : Any, Filters : Any, Item> PageLoadResult.Companion.page(
    id: String = Uuid.random().toHexString(),
    dataSourceId: String? = null,
    pageCursor: Cursor? = null,
    info: PageInfo = PageInfo(),
    items: List<Item> = emptyList(),
    original: PageRequest<Data, Filters>,
    actual: PageRequest<Data, Filters> = original
): DefaultResolvedPageWithRequestData<Data, Filters, Item> = ResolvedPage.invoke(
    id = id,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor,
    info = info,
    items = items,
    original = original,
    actual = actual
)

/**
 * A convenience function for creating a [DefaultPageCollection] instance.
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline fun <Item> PageLoadResult.Companion.pageCollection(
    id: String = Uuid.random().toHexString(),
    pageCursor: Cursor,
    pages: List<ResolvedPage<Item>> = emptyList(),
    dataSourceId: String? = null,
): DefaultPageCollection<Item> = PageCollection.invoke(
    id = id,
    pages = pages,
    dataSourceId = dataSourceId,
    pageCursor = pageCursor
)

/**
 * A convenience function for creating a [DefaultPagePlaceholder] instance.
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public inline fun <Item> PageLoadResult.Companion.pagePlaceholder(
    id: String = Uuid.random().toHexString(),
    pageCursor: Cursor? = null,
    noinline getter: suspend () -> ResolvedPage<Item>
): DefaultPagePlaceholder<Item> = PagePlaceholder.invoke(
    id = id,
    pageCursor = pageCursor,
    getter = getter
)

/**
 * Represents an invalid result. This mimics the class from the androidx.paging library. This is
 * used to indicate that the [PagedDataSource] is invalidated, perhaps due to data inconsistencies,
 * and should not be used again.
 */
@ExperimentalPaginationAPI
public data object InvalidResult : PageLoadResult<Nothing>

/**
 * Represents an error result. This mimics the class from the androidx.paging library.
 *
 * @property [cause] The exception that was encountered when attempting to load paged data.
 */
@ExperimentalPaginationAPI
public data class ErrorResult<Item> public constructor(
    public val cause: Throwable
) : PageLoadResult<Item>

/**
 * Returns `true` if this instance represents a successful outcome, otherwise `false`.
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalContracts::class)
public fun <Item> PageLoadResult<Item>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Page<Item>)
    }

    return this is Page<Item>
}

/**
 * Returns [Page.get] if this [PageLoadResult] is a [Page], otherwise `null`.
 */
@ExperimentalPaginationAPI
public suspend inline fun <Item> PageLoadResult<Item>.getOrNull(): List<Item>? =
    when {
        this is Page<Item> -> this.get()
        else -> null
    }

/**
 * Returns the [ErrorResult.cause] if this [PageLoadResult] is an [ErrorResult], otherwise `null`.
 */
@ExperimentalPaginationAPI
public inline fun PageLoadResult<*>.exceptionOrNull(): Throwable? =
    when {
        this is ErrorResult<*> -> this.cause
        else -> null
    }

/**
 * Returns [Page.get] if this [PageLoadResult] is a [Page], otherwise throws an exception.
 */
@ExperimentalPaginationAPI
public suspend inline fun <Item> PageLoadResult<Item>.getOrThrow(): List<Item> =
    when (this) {
        is Page<Item> -> this.get()
        is ErrorResult<*> -> throw this.cause
        else -> error("PageLoadResult was not a Page.")
    }
