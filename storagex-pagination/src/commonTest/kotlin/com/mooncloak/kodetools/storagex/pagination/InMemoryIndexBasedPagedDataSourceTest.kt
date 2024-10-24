package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalPaginationAPI::class)
class InMemoryIndexBasedPagedDataSourceTest {

    @Test
    fun values_are_correct() {
        val values = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val source = PagedDataSource.of<Nothing, Nothing, Int>(values = values)

        assertEquals(expected = values, actual = source.values)
    }

    @Test
    fun source_id_is_correct() {
        val sourceId = "ID123"
        val source = PagedDataSource.of<Nothing, Nothing, Int>(
            sourceId = sourceId,
            values = listOf(0, 1, 2, 3)
        )

        assertEquals(expected = sourceId, actual = source.sourceId)
    }

    @Test
    fun all_values_are_returned() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val result = source.load(request = PageRequest(count = 10u))

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = values, actual = result.items)
        }
    }

    @Test
    fun all_values_are_returned_for_default_request_with_values_less_than_default_count() {
        runTest {
            val values = listOf(
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
                13,
                14,
                15,
                16,
                17,
                18,
                19,
                20,
                21,
                22,
                23,
                24,
                25
            )
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val result = source.load(request = PageRequest())

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = values, actual = result.items)
        }
    }

    @Test
    fun all_values_except_last_are_returned_for_default_request_with_values_one_more_than_default_count() {
        runTest {
            val values = listOf(
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12,
                13,
                14,
                15,
                16,
                17,
                18,
                19,
                20,
                21,
                22,
                23,
                24,
                25,
                26
            )
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val result = source.load(request = PageRequest())

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = values.dropLast(1), actual = result.items)
        }
    }

    @Test
    fun first_page() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val result = source.load(request = PageRequest(count = 5u))
            val expected = listOf(1, 2, 3, 4, 5)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }

    @Test
    fun second_page() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 5u,
                    count = 5u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 5u,
                    cursor = cursor
                )
            )
            val expected = listOf(6, 7, 8, 9, 10)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }

    @Test
    fun count_from_request_is_used_over_count_from_cursor() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 5u,
                    count = 3u // This value should be ignored
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 5u, // This value should be used
                    cursor = cursor
                )
            )
            val expected = listOf(6, 7, 8, 9, 10)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }

    @Test
    fun requesting_more_than_there_are_items_returns_all_remaining_values() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 5u,
                    count = 20u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 20u,
                    cursor = cursor
                )
            )
            val expected = listOf(6, 7, 8, 9, 10)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }

    @Test
    fun offset_equals_size_returns_error() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 10u,
                    count = 1u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 1u,
                    cursor = cursor
                )
            )

            assertIs<ErrorResult<Int>>(result)
        }
    }

    @Test
    fun offset_one_greater_than_size_returns_error() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 11u,
                    count = 1u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 1u,
                    cursor = cursor
                )
            )

            assertIs<ErrorResult<Int>>(result)
        }
    }

    @Test
    fun offset_one_less_than_size_returns_value() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 9u,
                    count = 1u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 1u,
                    cursor = cursor
                )
            )
            val expected = listOf(10)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }

    @Test
    fun first_page_info_is_as_expected() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val result = source.load(request = PageRequest(count = 5u))

            val firstCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = 0u,
                    count = 5u
                )
            )
            val lastCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = 5u,
                    count = 5u
                )
            )

            val expectedInfo = PageInfo(
                hasPrevious = false,
                hasNext = true,
                firstCursor = firstCursor,
                lastCursor = lastCursor,
                totalCount = 10u
            )

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expectedInfo, actual = result.info)
        }
    }

    @Test
    fun second_page_info_is_as_expected() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values
            )
            val cursor = Cursor.encode(
                OffsetDecodedCursor(
                    offset = 5u,
                    count = 5u
                )
            )
            val result = source.load(
                request = PageRequest(
                    count = 5u,
                    cursor = cursor
                )
            )

            val firstCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = 5u,
                    count = 5u
                )
            )
            val lastCursor = Cursor.encode(
                value = OffsetDecodedCursor(
                    offset = 10u,
                    count = 5u
                )
            )

            val expectedInfo = PageInfo(
                hasPrevious = true,
                hasNext = false,
                firstCursor = firstCursor,
                lastCursor = lastCursor,
                totalCount = 10u
            )

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expectedInfo, actual = result.info)
        }
    }

    @Test
    fun only_filtered_values_are_returned() {
        runTest {
            val values = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val source = PagedDataSource.of<Nothing, Nothing, Int>(
                values = values,
                filter = { _ ->
                    this.subList(fromIndex = 0, toIndex = this.size / 2)
                }
            )
            val result = source.load(request = PageRequest(count = 10u))
            val expected = listOf(1, 2, 3, 4, 5)

            assertIs<ResolvedPage<Int>>(result)
            assertEquals(expected = expected, actual = result.items)
        }
    }
}
