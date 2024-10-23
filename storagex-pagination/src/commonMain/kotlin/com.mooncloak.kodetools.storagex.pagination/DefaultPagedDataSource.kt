package com.mooncloak.kodetools.storagex.pagination

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Retrieves a [PagedDataSource] whose [PagedDataSource.load] function delegates to the provided
 * [load] function. This is a convenience function for simply creating a [PagedDataSource].
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> PagedDataSource.Companion.of(
    sourceId: String = Uuid.random().toHexString(),
    load: suspend (request: PageRequest<Data, Filters>) -> PageLoadResult<Item>
): PagedDataSource<Data, Filters, Item> = DefaultPagedDataSource(
    sourceId = sourceId,
    load = load
)

/**
 * Retrieves a [PagedDataSource] whose [PagedDataSource.load] always returns the provided [result].
 * This is a convenience function for simply creating a [PagedDataSource] that always returns a
 * single value. This could be useful for testing purposes.
 */
@OptIn(ExperimentalUuidApi::class)
@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> PagedDataSource.Companion.of(
    sourceId: String = Uuid.random().toHexString(),
    result: PageLoadResult<Item>
): PagedDataSource<Data, Filters, Item> = DefaultPagedDataSource(
    sourceId = sourceId,
    load = { result }
)

@ExperimentalPaginationAPI
internal class DefaultPagedDataSource<Data : Any, Filters : Any, Item> internal constructor(
    override val sourceId: String,
    private val load: suspend (request: PageRequest<Data, Filters>) -> PageLoadResult<Item>
) : PagedDataSource<Data, Filters, Item> {

    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> =
        this.load.invoke(request)
}
