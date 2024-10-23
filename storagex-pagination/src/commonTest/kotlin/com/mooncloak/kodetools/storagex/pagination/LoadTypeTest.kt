package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalPaginationAPI::class)
class LoadTypeTest {

    @Test
    fun expected_values() {
        assertContains(LoadType.entries, LoadType.Append)
        assertContains(LoadType.entries, LoadType.Prepend)
        assertContains(LoadType.entries, LoadType.Refresh)
        assertEquals(
            expected = 3,
            actual = LoadType.entries.size
        )
    }

    @Test
    fun prepend_serial_name() {
        assertEquals(expected = "prepend", actual = LoadType.Prepend.serialName)
        assertEquals(expected = LoadType.Prepend, actual = LoadType["prepend"])
        assertEquals(expected = LoadType.Prepend, actual = LoadType["PREPEND"])
        assertEquals(expected = LoadType.Prepend, actual = LoadType["Prepend"])
    }

    @Test
    fun refresh_serial_name() {
        assertEquals(expected = "refresh", actual = LoadType.Refresh.serialName)
        assertEquals(expected = LoadType.Refresh, actual = LoadType["refresh"])
        assertEquals(expected = LoadType.Refresh, actual = LoadType["REFRESH"])
        assertEquals(expected = LoadType.Refresh, actual = LoadType["Refresh"])
    }

    @Test
    fun append_serial_name() {
        assertEquals(expected = "append", actual = LoadType.Append.serialName)
        assertEquals(expected = LoadType.Append, actual = LoadType["append"])
        assertEquals(expected = LoadType.Append, actual = LoadType["APPEND"])
        assertEquals(expected = LoadType.Append, actual = LoadType["Append"])
    }

    @Test
    fun unknown_serial_name() {
        assertNull(LoadType["unknown"])
    }
}
