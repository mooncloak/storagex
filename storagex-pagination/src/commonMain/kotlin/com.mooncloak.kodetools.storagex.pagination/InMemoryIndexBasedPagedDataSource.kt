package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Creates an [InMemoryIndexBasedPagedDataSource] with the provided [values].
 *
 * @param [values] The [Collection] of values.
 *
 * @param [sourceId] The identifier of the returned [InMemoryIndexBasedPagedDataSource].
 *
 * @param [format] The [StringFormat] used for decoding and encoding the [Cursor] values.
 *
 * @param [filter] An optional filter function that is invoked while loading each page.
 *
 * @return [InMemoryIndexBasedPagedDataSource]
 *
 * ## Example Usage:
 *
 * ```
 * val source = PagedDataSource.of<Query, Filters, Item>(values = myValuesList)
 * ```
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalUuidApi::class)
public inline fun <Data : Any, Filters : Any, Item> PagedDataSource.Companion.of(
    values: Collection<Item>,
    sourceId: String = Uuid.random().toHexString(),
    format: StringFormat = Json.Default,
    noinline filter: List<Item>.(request: PageRequest<Data, Filters>) -> List<Item> = { _ -> this }
): InMemoryIndexBasedPagedDataSource<Data, Filters, Item> = InMemoryIndexBasedPagedDataSource(
    sourceId = sourceId,
    values = values.toList(),
    format = format,
    filter = filter
)

/**
 * A [PagedDataSource] that keeps the provided [values] in-memory (not stored to disk). Invoking
 * the [InMemoryIndexBasedPagedDataSource.load] function, loads the pages from the [values]
 * in-memory.
 */
@ExperimentalPaginationAPI
public class InMemoryIndexBasedPagedDataSource<Data : Any, Filters : Any, Item> public constructor(
    override val sourceId: String,
    public val values: List<Item>,
    private val format: StringFormat,
    private val filter: List<Item>.(request: PageRequest<Data, Filters>) -> List<Item>
) : PagedDataSource<Data, Filters, Item> {

    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> {
        val decodedCursor = request.cursor?.decodeOrNull<OffsetDecodedCursor>()
        val offset = decodedCursor?.offset?.toInt() ?: 0
        val count = request.count.toInt()
        val filteredValues = values.filter(request)

        if (offset >= filteredValues.size) {
            return PageLoadResult.error(cause = IndexOutOfBoundsException())
        } else {
            val toIndex = (offset + count).coerceAtMost(filteredValues.size)
            val items = filteredValues.subList(fromIndex = offset, toIndex = toIndex)
            val firstCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = offset.toUInt(),
                    count = count.toUInt()
                ),
                format = format
            )
            val lastCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = (offset + count).toUInt(),
                    count = count.toUInt()
                ),
                format = format
            )

            return PageLoadResult.page(
                dataSourceId = sourceId,
                items = items,
                info = PageInfo(
                    hasPrevious = offset > 0,
                    hasNext = offset + count < filteredValues.size,
                    firstCursor = firstCursor,
                    lastCursor = lastCursor,
                    totalCount = filteredValues.size.toUInt()
                )
            )
        }
    }
}
