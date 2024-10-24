package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalPaginationAPI::class)
class CombinedDecodedPageCursorTest {

    @Test
    fun encoding_and_decoding_with_string_format_works() {
        val format = Json.Default
        val original = CombinedDecodedPageCursor(
            references = listOf(
                PageReference(
                    id = "123",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = true
                    )
                ),
                PageReference(
                    id = "456",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = false
                    )
                )
            )
        )
        val encoded = Cursor.encode(
            value = original,
            format = format
        )
        val decoded = encoded.decode<CombinedDecodedPageCursor>(
            format = format
        )

        assertEquals(expected = original, actual = decoded)
    }

    @Test
    fun encoding_with_extension_function_and_decoding_with_string_format_works() {
        val format = Json.Default
        val expected = CombinedDecodedPageCursor(
            references = listOf(
                PageReference(
                    id = "123",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = true
                    )
                ),
                PageReference(
                    id = "456",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = false
                    )
                )
            )
        )
        val encoded = Cursor.encode(
            pages = listOf(
                ResolvedPage<String>(
                    id = "123",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = true
                    )
                ),
                ResolvedPage<String>(
                    id = "456",
                    dataSourceId = "sourceId",
                    info = PageInfo(
                        hasNext = false
                    )
                )
            ),
            format = format
        )
        val decoded = encoded.decode<CombinedDecodedPageCursor>(
            format = format
        )

        assertEquals(expected = expected, actual = decoded)
    }
}
