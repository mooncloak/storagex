package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json

/**
 * A model that can be encoded as a [Cursor] for offset pagination.
 *
 * @property [offset] The amount of items to offset when retrieving the page.
 *
 * @property [count] The amount of items to retrieve.
 */
@Serializable
@ExperimentalPaginationAPI
public data class OffsetDecodedCursor public constructor(
    @SerialName(value = "offset") public val offset: UInt,
    @SerialName(value = "count") public val count: UInt
)

/**
 * Encodes the provided values as an [OffsetDecodedCursor] model in its encoded form, using the
 * provided [format].
 *
 * @param [offset] The amount of items to offset when retrieving the page.
 *
 * @param [count] The amount of items to retrieve.
 *
 * @param [format] The [StringFormat] used to encode the constructed [OffsetDecodedCursor] model
 * into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [OffsetDecodedCursor]
 * @see [Cursor.Companion.encode]
 */
@ExperimentalPaginationAPI
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun Cursor.Companion.encode(
    offset: UInt,
    count: UInt = PageRequest.DEFAULT_COUNT,
    format: StringFormat = Json.Default
): Cursor {
    val decoded = OffsetDecodedCursor(
        offset = offset,
        count = count
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = OffsetDecodedCursor.serializer()
    )
}

/**
 * Encodes the provided values as an [OffsetDecodedCursor] model in its encoded form, using the
 * provided [format].
 *
 * @param [offset] The amount of items to offset when retrieving the page.
 *
 * @param [count] The amount of items to retrieve.
 *
 * @param [format] The [BinaryFormat] used to encode the constructed [OffsetDecodedCursor] model
 * into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [OffsetDecodedCursor]
 * @see [Cursor.Companion.encode]
 */
@ExperimentalPaginationAPI
@Throws(SerializationException::class, IllegalArgumentException::class)
public inline fun Cursor.Companion.encode(
    offset: UInt,
    count: UInt = PageRequest.DEFAULT_COUNT,
    format: BinaryFormat
): Cursor {
    val decoded = OffsetDecodedCursor(
        offset = offset,
        count = count
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = OffsetDecodedCursor.serializer()
    )
}
