package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalPaginationAPI::class)
class PageRequestTest {

    @Test
    fun default_count_is_expected_value() {
        assertEquals(expected = PageRequest.DEFAULT_COUNT, actual = 25u)
    }

    @Test
    fun default_page_request_uses_default_count() {
        val request = PageRequest<Nothing, Nothing>()

        assertEquals(expected = PageRequest.DEFAULT_COUNT, actual = request.count)
    }

    @Test
    fun default_data_is_null() {
        val request = PageRequest<Nothing, Nothing>()

        assertNull(request.data)
    }

    @Test
    fun default_direction_is_after() {
        val request = PageRequest<Nothing, Nothing>()

        assertEquals(expected = Direction.After, actual = request.direction)
    }

    @Test
    fun default_cursor_is_null() {
        val request = PageRequest<Nothing, Nothing>()

        assertNull(request.cursor)
    }

    @Test
    fun default_sort_is_null() {
        val request = PageRequest<Nothing, Nothing>()

        assertNull(request.sort)
    }

    @Test
    fun default_filters_is_null() {
        val request = PageRequest<Nothing, Nothing>()

        assertNull(request.filters)
    }

    @Test
    fun two_defaults_of_the_same_types_equal_each_other() {
        val defaultOne = PageRequest<Nothing, Nothing>()
        val defaultTwo = PageRequest<Nothing, Nothing>()

        assertEquals(defaultOne, defaultTwo)
    }
}
