package com.mooncloak.kodetools.storagex.pagination

/**
 * A [PagedDataSource] whose [PagedDataSource.load] function always returns an empty [Page].
 */
@ExperimentalPaginationAPI
public class EmptyPagedDataSource<Data : Any, Filters : Any, Item> public constructor(
    override val sourceId: String = "EmptyPagedDataSource"
) : PagedDataSource<Data, Filters, Item> {

    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> =
        PageLoadResult.emptyPage(dataSourceId = sourceId)
}
