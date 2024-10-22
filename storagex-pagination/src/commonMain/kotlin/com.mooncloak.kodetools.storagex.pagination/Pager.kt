package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * A stateful abstraction over one or more [PagedDataSource] components.
 */
@ExperimentalPaginationAPI
public interface Pager<Item> {

    /**
     * The [Flow] of [PagerStateModel]s that get emitted when the data is updated by invoking the
     * paging functions.
     */
    public val flow: Flow<PagerStateModel<Item>>

    /**
     * Refreshes the data by loading the initial paging data.
     */
    public suspend fun refresh()

    /**
     * Loads at the start of the paging data.
     */
    public suspend fun prepend()

    /**
     * Loads at the end of the paging data.
     */
    public suspend fun append()

    public fun interface Loader<Data : Any, Filters : Any, Item> {

        public fun load(
            request: PageRequest<Data, Filters>,
            coroutineScope: CoroutineScope
        ): Pager<Item>

        public companion object
    }

    public companion object
}

@ExperimentalPaginationAPI
public suspend fun <Data : Any, Filters : Any, Item> Pager.Loader<Data, Filters, Item>.load(
    request: PageRequest<Data, Filters>
): Pager<Item> = coroutineScope {
    this@load.load(
        request = request,
        coroutineScope = this
    )
}

@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Pager.Loader.Companion.create(
    source: PagedDataSource<Data, Filters, Item>
): Pager.Loader<Data, Filters, Item> = DefaultPagerLoader(
    source = source
)

@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Pager.Loader.Companion.create(
    vararg sources: PagedDataSource<Data, Filters, Item>
): Pager.Loader<Data, Filters, Item> = when {
    sources.isEmpty() -> error("At least one `PagedDataSource` MUST be provided to the `Pager.Loader.create` function.")
    sources.size == 1 -> DefaultPagerLoader(source = sources.first())
    else -> DefaultPagerLoader(source = CombinedPagedDataSource(sources = sources.toList()))
}

@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Pager.Loader.Companion.create(
    sources: Collection<PagedDataSource<Data, Filters, Item>>
): Pager.Loader<Data, Filters, Item> = when {
    sources.isEmpty() -> error("At least one `PagedDataSource` MUST be provided to the `Pager.Loader.create` function.")
    sources.size == 1 -> DefaultPagerLoader(source = sources.first())
    else -> DefaultPagerLoader(source = CombinedPagedDataSource(sources = sources.toList()))
}
