package com.mooncloak.kodetools.storagex.pagination

/**
 * A stateless repository abstraction over a data source whose underlying data can be paginated through.
 */
public interface PagedDataRepository<Request, Filters, Result> {

    public val sourceId: String

    public suspend fun load(
        request: Request,
        count: UInt = 20u,
        cursor: Cursor? = null,
        direction: Direction = Direction.After,
        sort: SortOptions? = null,
        filters: Filters? = null
    ): ResolvedPage<Result>

    public suspend fun load(
        cursor: Cursor,
        direction: Direction
    ): ResolvedPage<Result>

    public companion object
}

public suspend inline fun <Result> PagedDataRepository<*, *, Result>.before(
    cursor: Cursor
): ResolvedPage<Result> = this.load(
    cursor = cursor,
    direction = Direction.Before
)

public suspend inline fun <Result> PagedDataRepository<*, *, Result>.after(
    cursor: Cursor
): ResolvedPage<Result> = this.load(
    cursor = cursor,
    direction = Direction.After
)
