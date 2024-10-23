package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.CoroutineScope

@ExperimentalPaginationAPI
internal class DefaultPagerLoader<Data : Any, Filters : Any, Item> internal constructor(
    private val source: PagedDataSource<Data, Filters, Item>
) : Pager.Loader<Data, Filters, Item> {

    override fun load(
        request: PageRequest<Data, Filters>,
        coroutineScope: CoroutineScope
    ): Pager<Item> = DefaultPager(
        initialCoroutineScope = coroutineScope,
        initialRequest = request,
        source = source
    )
}
