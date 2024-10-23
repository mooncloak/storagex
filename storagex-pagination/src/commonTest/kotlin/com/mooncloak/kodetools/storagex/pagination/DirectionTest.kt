package com.mooncloak.kodetools.storagex.pagination

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalPaginationAPI::class)
class DirectionTest {

    @Test
    fun expected_values() {
        assertContains(Direction.entries, Direction.Before)
        assertContains(Direction.entries, Direction.After)
        assertEquals(
            expected = 2,
            actual = Direction.entries.size
        )
    }

    @Test
    fun before_serial_name() {
        assertEquals(expected = "before", actual = Direction.Before.serialName)
        assertEquals(expected = Direction.Before, actual = Direction["before"])
        assertEquals(expected = Direction.Before, actual = Direction["BEFORE"])
        assertEquals(expected = Direction.Before, actual = Direction["Before"])
    }

    @Test
    fun after_serial_name() {
        assertEquals(expected = "after", actual = Direction.After.serialName)
        assertEquals(expected = Direction.After, actual = Direction["after"])
        assertEquals(expected = Direction.After, actual = Direction["AFTER"])
        assertEquals(expected = Direction.After, actual = Direction["After"])
    }

    @Test
    fun unknown_serial_name() {
        assertNull(Direction["unknown"])
    }
}
