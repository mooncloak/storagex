package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalPaginationAPI::class)
class OffsetDecodedCursorTest {

    @Test
    fun encoding_and_decoding_with_string_format_works() {
        val format = Json.Default
        val original = OffsetDecodedCursor(
            offset = 10u,
            count = 20u
        )
        val encoded = Cursor.encode(
            value = original,
            format = format
        )
        val decoded = encoded.decode<OffsetDecodedCursor>(format = format)

        assertEquals(expected = original, actual = decoded)
        assertEquals(expected = 10u, actual = decoded.offset)
        assertEquals(expected = 20u, actual = decoded.count)
    }

    @Test
    fun encoding_with_extension_function_and_decoding_with_string_format_works() {
        val format = Json.Default
        val expected = OffsetDecodedCursor(
            offset = 10u,
            count = 20u
        )
        val encoded = Cursor.encode(
            offset = 10u,
            count = 20u,
            format = format
        )
        val decoded = encoded.decode<OffsetDecodedCursor>(format = format)

        assertEquals(expected = expected, actual = decoded)
        assertEquals(expected = 10u, actual = decoded.offset)
        assertEquals(expected = 20u, actual = decoded.count)
    }
}
