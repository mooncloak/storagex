package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalPaginationAPI::class)
class EmptyPagedDataSourceTest {

    @Test
    fun expected_default_source_id() {
        val source = EmptyPagedDataSource<Nothing, Nothing, String>()

        assertEquals(expected = "EmptyPagedDataSource", actual = source.sourceId)
    }

    @Test
    fun custom_source_id() {
        val source = EmptyPagedDataSource<Nothing, Nothing, String>(sourceId = "Custom")

        assertEquals(expected = "Custom", actual = source.sourceId)
    }

    @Test
    fun load_returns_empty_page_for_empty_request() {
        runTest {
            val result = load<Nothing, Nothing, String>(
                request = PageRequest()
            )

            assertIs<ResolvedPage<String>>(result)
            assertEquals(expected = 0, actual = result.items.size)
        }
    }

    @Test
    fun load_returns_empty_page_for_request_with_data() {
        runTest {
            val result = load<String, String, String>(
                request = PageRequest(
                    data = "Testing123",
                    filters = "Filters123"
                )
            )

            assertIs<ResolvedPage<String>>(result)
            assertEquals(expected = 0, actual = result.items.size)
        }
    }

    private suspend fun <Data : Any, Filters : Any, Item> load(request: PageRequest<Data, Filters>): PageLoadResult<Item> {
        val source = EmptyPagedDataSource<Data, Filters, Item>()

        return source.load(request = request)
    }
}
