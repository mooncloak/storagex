package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmInline

/**
 * Serves a similar purpose to a [Cursor] but for [Page]s instead of items in a [Page]. This is a
 * distinct type for explicit distinction between the two use-cases.
 */
@ExperimentalPaginationAPI
@JvmInline
@Serializable
public value class PageCursor public constructor(
    public val value: String
) {

    public companion object
}

@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified DecodedCursor> PageCursor.Companion.encode(
    value: DecodedCursor,
    format: StringFormat
): PageCursor {
    val formatString = format.encodeToString(value = value)
    val base64EncodedString = Base64.UrlSafe.encode(formatString.encodeToByteArray())

    return PageCursor(value = base64EncodedString)
}

@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified DecodedCursor> PageCursor.decode(format: StringFormat): DecodedCursor {
    val bytes = Base64.UrlSafe.decode(this.value)
    val formatString = bytes.decodeToString()

    return format.decodeFromString(string = formatString)
}

@ExperimentalPaginationAPI
internal fun <Item> PageCursor.Companion.encodeCombined(pages: List<ResolvedPage<Item>>): PageCursor {
    val decoded = CombinedDecodedPageCursor(
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info
            )
        }
    )

    return PageCursor.encode(
        value = decoded,
        format = Json.Default
    )
}

@ExperimentalPaginationAPI
internal fun PageCursor.decodeCombined(): CombinedDecodedPageCursor =
    this.decode(format = Json.Default)

@ExperimentalPaginationAPI
@Serializable
internal data class CombinedDecodedPageCursor internal constructor(
    @SerialName(value = "references") internal val references: List<PageReference> = emptyList()
)

@ExperimentalPaginationAPI
@Serializable
internal data class PageReference internal constructor(
    @SerialName(value = "id") internal val id: String,
    @SerialName(value = "info") internal val info: PageInfo
)
