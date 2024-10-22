package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.flow.StateFlow

/**
 * A stateful abstraction over one or more [PagedDataRepository] components.
 */
@ExperimentalPaginationAPI
public interface Pager<Request, Filters, Result> {

    public val loading: StateFlow<Boolean>

    public val pages: StateFlow<List<Page<Result>>>

    public suspend fun load(
        request: Request,
        count: UInt = 20u,
        cursor: Cursor? = null,
        direction: Direction = Direction.After,
        sort: SortOptions? = null,
        filters: Filters? = null
    )

    public suspend fun invalidate()

    public suspend fun refresh()

    public suspend fun previous()

    public suspend fun next()

    public companion object
}

@ExperimentalPaginationAPI
public suspend inline fun <Request, Filters, Result> Pager<Request, Filters, Result>.load(
    pageRequest: PageRequest<Request, Filters>
): Unit = load(
    request = pageRequest.request,
    count = pageRequest.count,
    cursor = pageRequest.cursor,
    direction = pageRequest.direction,
    sort = pageRequest.sort,
    filters = pageRequest.filters
)

@ExperimentalPaginationAPI
public operator fun <Request, Filters, Result> Pager.Companion.invoke(
    sources: List<PagedDataRepository<Request, Filters, Result>>
): Pager<Request, Filters, Result> = MultipleDataSourcePager(
    sources = sources
)
