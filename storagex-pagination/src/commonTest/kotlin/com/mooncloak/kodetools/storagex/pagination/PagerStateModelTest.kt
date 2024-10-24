package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalPaginationAPI::class)
class PagerStateModelTest {

    @Test
    fun pages_defaults_to_empty_list() {
        val state = PagerStateModel<String>()

        assertEquals(expected = 0, actual = state.pages.size)
        assertEquals(expected = emptyList(), actual = state.pages)
    }

    @Test
    fun prepend_defaults_to_incomplete() {
        val state = PagerStateModel<String>()

        assertEquals(expected = LoadState.Incomplete, actual = state.prepend)
    }

    @Test
    fun append_defaults_to_incomplete() {
        val state = PagerStateModel<String>()

        assertEquals(expected = LoadState.Incomplete, actual = state.append)
    }

    @Test
    fun refresh_defaults_to_incomplete() {
        val state = PagerStateModel<String>()

        assertEquals(expected = LoadState.Incomplete, actual = state.refresh)
    }
}
