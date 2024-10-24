package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.jvm.JvmInline

/**
 * Represents a generic pointer to a page or an item in a paginated list. This serves as a
 * reference to an item, so that subsequent requests can load items before or after that item.
 *
 * @property [value] The cursor [String] value. This is an opaque [String] value from the
 * perspective of the client, as it doesn't have to construct an instance of one, only use it as a
 * reference.
 */
@ExperimentalPaginationAPI
@JvmInline
@Serializable
public value class Cursor public constructor(
    @Suppress("MemberVisibilityCanBePrivate") public val value: String
) {

    public companion object
}

/**
 * Encodes the provided [value] into a [Cursor] using the provided String [format] and Base64 URL
 * encoding, or throws an exception if the encoding failed.
 *
 * @param [value] The value to encode to a [Cursor].
 *
 * @param [format] The [StringFormat] used to convert the provided [value] to a [String].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] containing an encoded form of the [value].
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun <reified DecodedCursor> Cursor.Companion.encode(
    value: DecodedCursor,
    format: StringFormat = Json.Default,
    serializer: SerializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): Cursor {
    val formatString = format.encodeToString(value = value, serializer = serializer)
    val base64EncodedString = Base64.UrlSafe.encode(formatString.encodeToByteArray())

    return Cursor(value = base64EncodedString)
}

/**
 * Decodes this [Cursor] into the [DecodedCursor] type using the provided String [format] and
 * Base64 URL decoding, or throws an exception if this [Cursor] does not represent the
 * [DecodedCursor] value.
 *
 * @param [format] The [StringFormat] used to convert this [Cursor] to a [DecodedCursor].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return The [DecodedCursor] value.
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun <reified DecodedCursor> Cursor.decode(
    format: StringFormat = Json.Default,
    deserializer: DeserializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): DecodedCursor {
    val bytes = Base64.UrlSafe.decode(this.value)
    val formatString = bytes.decodeToString()

    return format.decodeFromString(string = formatString, deserializer = deserializer)
}

/**
 * Decodes this [Cursor] into the [DecodedCursor] type using the provided String [format] and
 * Base64 URL decoding, or returns `null` if this [Cursor] does not represent the [DecodedCursor]
 * value.
 *
 * @param [format] The [StringFormat] used to convert this [Cursor] to a [DecodedCursor].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return The [DecodedCursor] value.
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
public inline fun <reified DecodedCursor> Cursor.decodeOrNull(
    format: StringFormat = Json.Default,
    deserializer: DeserializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): DecodedCursor? = try {
    this.decode(
        format = format,
        deserializer = deserializer
    )
} catch (_: Exception) {
    null
}

/**
 * Encodes the provided [value] into a [Cursor] using the provided Binary [format] and Base64 URL
 * encoding, or throws an exception if the encoding failed.
 *
 * @param [value] The value to encode to a [Cursor].
 *
 * @param [format] The [BinaryFormat] used to convert the provided [value] to a [String].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] containing an encoded form of the [value].
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun <reified DecodedCursor> Cursor.Companion.encode(
    value: DecodedCursor,
    format: BinaryFormat,
    serializer: SerializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): Cursor {
    val encodedBytes = format.encodeToByteArray(value = value, serializer = serializer)
    val base64EncodedString = Base64.UrlSafe.encode(encodedBytes)

    return Cursor(value = base64EncodedString)
}

/**
 * Decodes this [Cursor] into the [DecodedCursor] type using the provided Binary [format] and
 * Base64 URL decoding, or throws an exception if this [Cursor] does not represent the
 * [DecodedCursor] value.
 *
 * @param [format] The [BinaryFormat] used to convert this [Cursor] to a [DecodedCursor].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return The [DecodedCursor] value.
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
@OptIn(ExperimentalEncodingApi::class)
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun <reified DecodedCursor> Cursor.decode(
    format: BinaryFormat,
    deserializer: DeserializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): DecodedCursor {
    val bytes = Base64.UrlSafe.decode(this.value)

    return format.decodeFromByteArray(bytes = bytes, deserializer = deserializer)
}

/**
 * Decodes this [Cursor] into the [DecodedCursor] type using the provided Binary [format] and
 * Base64 URL decoding, or returns `null` if this [Cursor] does not represent the [DecodedCursor]
 * value.
 *
 * @param [format] The [BinaryFormat] used to convert this [Cursor] to a [DecodedCursor].
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return The [DecodedCursor] value.
 *
 * @see [Cursor]
 */
@ExperimentalPaginationAPI
public inline fun <reified DecodedCursor> Cursor.decodeOrNull(
    format: BinaryFormat,
    deserializer: DeserializationStrategy<DecodedCursor> = format.serializersModule.serializer()
): DecodedCursor? = try {
    this.decode(
        format = format,
        deserializer = deserializer
    )
} catch (_: Exception) {
    null
}
