package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalPaginationAPI::class)
class CombinedPagedDataSourceTest {

    @Test
    fun first_page() {
        runTest {
            val dataOne = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val dataTwo = listOf(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
            val sourceOne = PagedDataSource.of<Nothing, Nothing, Int>(
                values = dataOne
            )
            val sourceTwo = PagedDataSource.of<Nothing, Nothing, Int>(
                values = dataTwo
            )
            val combinedSource = CombinedPagedDataSource(
                sources = listOf(sourceOne, sourceTwo)
            )
            val request = PageRequest<Nothing, Nothing>(
                count = 5u
            )

            val result = combinedSource.load(request)

            assertIs<PageCollection<Int>>(result)
            assertEquals(expected = 2, actual = result.pages.size)

            val pageOne = result.pages.first()
            val pageTwo = result.pages[1]

            assertEquals(expected = 5, actual = pageOne.items.size)
            assertEquals(expected = 5, actual = pageTwo.items.size)
            assertContentEquals(expected = listOf(1, 2, 3, 4, 5), actual = pageOne.items)
            assertContentEquals(expected = listOf(11, 12, 13, 14, 15), actual = pageTwo.items)
        }
    }

    @Test
    fun second_page() {
        runTest {
            val dataOne = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            val dataTwo = listOf(11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
            val sourceOne = PagedDataSource.of<Nothing, Nothing, Int>(
                values = dataOne
            )
            val sourceTwo = PagedDataSource.of<Nothing, Nothing, Int>(
                values = dataTwo
            )
            val combinedSource = CombinedPagedDataSource(
                sources = listOf(sourceOne, sourceTwo)
            )
            val initialRequest = PageRequest<Nothing, Nothing>(
                count = 5u
            )

            val initialResult = combinedSource.load(initialRequest)

            assertIs<PageCollection<Int>>(initialResult)
            assertEquals(expected = 2, actual = initialResult.pages.size)

            val request = PageRequest<Nothing, Nothing>(
                cursor = initialResult.pageCursor
            )

            val result = combinedSource.load(request)

            assertIs<PageCollection<Int>>(result)
            assertEquals(expected = 2, actual = result.pages.size)

            val pageOne = result.pages.first()
            val pageTwo = result.pages[1]

            assertEquals(expected = 5, actual = pageOne.items.size)
            assertEquals(expected = 5, actual = pageTwo.items.size)
            assertContentEquals(expected = listOf(6, 7, 8, 9, 10), actual = pageOne.items)
            assertContentEquals(expected = listOf(16, 17, 18, 19, 20), actual = pageTwo.items)
        }
    }
}
