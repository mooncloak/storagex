package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test
import kotlin.test.assertNull

@OptIn(ExperimentalPaginationAPI::class)
class PageInfoTest {

    @Test
    fun has_previous_defaults_to_null() {
        val info = PageInfo()

        assertNull(info.hasPrevious)
    }

    @Test
    fun has_next_defaults_to_null() {
        val info = PageInfo()

        assertNull(info.hasNext)
    }

    @Test
    fun first_cursor_defaults_to_null() {
        val info = PageInfo()

        assertNull(info.firstCursor)
    }

    @Test
    fun last_cursor_defaults_to_null() {
        val info = PageInfo()

        assertNull(info.lastCursor)
    }

    @Test
    fun total_count_defaults_to_null() {
        val info = PageInfo()

        assertNull(info.totalCount)
    }
}
