package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalPaginationAPI::class)
class DefaultPagedDataSourceTest {

    @Test
    fun of_function_with_closure_returns_default_paged_data_source() {
        val source = PagedDataSource.of<Nothing, Nothing, String> { PageLoadResult.emptyPage() }

        assertIs<DefaultPagedDataSource<Nothing, Nothing, String>>(source)
    }

    @Test
    fun of_function_with_result_returns_default_paged_data_source() {
        val source = PagedDataSource.of<Nothing, Nothing, String>(
            result = PageLoadResult.emptyPage()
        )

        assertIs<DefaultPagedDataSource<Nothing, Nothing, String>>(source)
    }

    @Test
    fun of_function_with_result_load_function_returns_result() {
        runTest {
            val expectedResult = PageLoadResult.emptyPage<String>()
            val source = PagedDataSource.of<Nothing, Nothing, String>(
                result = expectedResult
            )

            val actualResult = source.load(request = PageRequest())

            assertEquals(expected = expectedResult, actual = actualResult)
        }
    }

    @Test
    fun of_function_with_result_load_function_with_data_returns_result() {
        runTest {
            val expectedResult = PageLoadResult.page(items = listOf("123", "456", "789"))
            val source = PagedDataSource.of<String, String, String>(
                result = expectedResult
            )

            val actualResult = source.load(
                request = PageRequest(
                    data = "Testing123",
                    filters = "Filters123"
                )
            )

            assertEquals(expected = expectedResult, actual = actualResult)
        }
    }
}
