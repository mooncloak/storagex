package com.mooncloak.kodetools.storagex.pagination

/**
 * A stateless repository abstraction over a data source whose underlying data can be paginated
 * through.
 */
@ExperimentalPaginationAPI
public interface PagedDataSource<Data : Any, Filters : Any, Item> {

    public val sourceId: String

    public suspend fun load(
        request: PageRequest<Data, Filters>
    ): PageLoadResult<Item>

    public companion object
}

@ExperimentalPaginationAPI
public suspend inline fun <Data : Any, Filters : Any, Item> PagedDataSource<Data, Filters, Item>.before(
    cursor: Cursor
): PageLoadResult<Item> = this.load(
    request = PageRequest(
        cursor = cursor,
        direction = Direction.Before
    )
)

@ExperimentalPaginationAPI
public suspend inline fun <Data : Any, Filters : Any, Item> PagedDataSource<Data, Filters, Item>.after(
    cursor: Cursor
): PageLoadResult<Item> = this.load(
    request = PageRequest(
        cursor = cursor,
        direction = Direction.After
    )
)
