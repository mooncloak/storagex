package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test

@OptIn(ExperimentalPaginationAPI::class)
class CombinedPagedDataSourceTest {

    @Test
    fun test() {
        val dataOne = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val dataTwo = listOf(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        val sourceOne = PagedDataSource.of<Nothing, Nothing, Int>(
            load = { request ->
                val decodedCursor = request.cursor?.decodeOrNull<OffsetDecodedCursor>()
                val offset = decodedCursor?.offset?.toInt() ?: 0
                val count = request.count.toInt()

                if (offset >= dataOne.size) {
                    PageLoadResult.error(cause = IndexOutOfBoundsException())
                } else {
                    val toIndex = (offset + count).coerceAtMost(dataOne.lastIndex)
                    val items = dataOne.subList(fromIndex = offset, toIndex = toIndex)

                    PageLoadResult.page(
                        items = items,
                        info = PageInfo(
                            hasPrevious = null,
                            hasNext = null,
                            firstCursor = null,
                            lastCursor = null,
                            totalCount = dataOne.size.toUInt()
                        )
                    )
                }
            }
        )
    }

    private fun <Data : Any, Filters : Any, Item> dataSource(
        allItems: List<Item>
    ): PagedDataSource<Data, Filters, Item> = PagedDataSource.of(
        load = { request ->
            val decodedCursor = request.cursor?.decodeOrNull<OffsetDecodedCursor>()
            val offset = decodedCursor?.offset?.toInt() ?: 0
            val count = request.count.toInt()

            if (offset >= allItems.size) {
                PageLoadResult.error(cause = IndexOutOfBoundsException())
            } else {
                val toIndex = (offset + count).coerceAtMost(allItems.size)
                val items = allItems.subList(fromIndex = offset, toIndex = toIndex)
                val firstCursor = Cursor.encode(
                    OffsetDecodedCursor(
                        offset = offset.toUInt(),
                        count = count.toUInt()
                    )
                )
                val lastCursor = Cursor.encode(
                    OffsetDecodedCursor(
                        offset = (offset + count).toUInt(),
                        count = count.toUInt()
                    )
                )

                PageLoadResult.page(
                    items = items,
                    info = PageInfo(
                        hasPrevious = offset > 0,
                        hasNext = offset + count < allItems.size,
                        firstCursor = firstCursor,
                        lastCursor = lastCursor,
                        totalCount = allItems.size.toUInt()
                    )
                )
            }
        }
    )
}
