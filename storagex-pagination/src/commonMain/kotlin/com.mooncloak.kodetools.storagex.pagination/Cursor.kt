package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmInline

/**
 * Represents a generic pointer to an item in a paginated list. This serves as a reference to an
 * item, so that subsequent requests can load items before or after that item.
 *
 * @property [value] The cursor [String] value. This is an opaque [String] value from the
 * perspective of the client, as it doesn't have to construct an instance of one, only use it as a
 * reference.
 */
@JvmInline
@Serializable
public value class Cursor public constructor(
    @Suppress("MemberVisibilityCanBePrivate") public val value: String
) {

    public companion object
}

@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified DecodedCursor> Cursor.Companion.encode(
    value: DecodedCursor,
    format: StringFormat
): FilterKey {
    val formatString = format.encodeToString(value = value)
    val base64EncodedString = Base64.UrlSafe.encode(formatString.encodeToByteArray())

    return FilterKey(value = base64EncodedString)
}

@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified DecodedCursor> Cursor.decode(format: StringFormat): DecodedCursor {
    val bytes = Base64.UrlSafe.decode(this.value)
    val formatString = bytes.decodeToString()

    return format.decodeFromString(string = formatString)
}
