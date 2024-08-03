package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmInline

/**
 * Represents an identifier to the filter values that were used for a pagination request.
 *
 * @property [value] The [String] value representing the key for the filter values. From the
 * perspective of the client, this is an opaque [String] value, as the client will only have to use
 * this value as a reference for subsequent pagination requests. However, from the perspective of
 * the server, this is a Base64 encoded [String] value representation of a serialization structure
 * (typically JSON) that represents the model of the filters applied. Filters can vary for
 * different pagination requests, so the call-site will need to know the type to decode this value.
 */
@Serializable
@JvmInline
public value class FilterKey public constructor(
    public val value: String
) {

    public companion object
}

@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified Filter> FilterKey.Companion.encode(
    value: Filter,
    format: StringFormat
): FilterKey {
    val formatString = format.encodeToString(value = value)
    val base64EncodedString = Base64.UrlSafe.encode(formatString.encodeToByteArray())

    return FilterKey(value = base64EncodedString)
}

@OptIn(ExperimentalEncodingApi::class)
public inline fun <reified Filter> FilterKey.decode(format: StringFormat): Filter {
    val bytes = Base64.UrlSafe.decode(this.value)
    val formatString = bytes.decodeToString()

    return format.decodeFromString(string = formatString)
}
