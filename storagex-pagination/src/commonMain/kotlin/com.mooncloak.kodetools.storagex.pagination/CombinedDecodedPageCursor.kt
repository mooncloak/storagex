package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@ExperimentalPaginationAPI
@Serializable
public data class CombinedDecodedPageCursor<Data : Any, Filters : Any> public constructor(
    @SerialName(value = "request") public val request: PageRequest<Data, Filters>,
    @SerialName(value = "references") public val references: List<PageReference> = emptyList()
)

@ExperimentalPaginationAPI
@Serializable
public data class PageReference public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "source_id") public val dataSourceId: String?,
    @SerialName(value = "info") public val info: PageInfo
)

/**
 * Encodes the provided values as a [CombinedDecodedPageCursor] model in its encoded form, using
 * the provided [format].
 *
 * @param [request] The [PageRequest] made to the underlying [PagedDataSource]s.
 *
 * @param [pages] The [ResolvedPage]s returned by the [PagedDataSource]s.
 *
 * @param [format] The [StringFormat] used to encode the constructed [CombinedDecodedPageCursor]
 * model into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [CombinedDecodedPageCursor]
 * @see [Cursor.Companion.encode]
 * @see [CombinedPagedDataSource]
 */
@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any, Item> Cursor.Companion.encode(
    request: PageRequest<Data, Filters>,
    pages: List<ResolvedPage<Item>>,
    format: StringFormat = Json.Default,
    serializer: SerializationStrategy<CombinedDecodedPageCursor<Data, Filters>> = format.serializersModule.serializer()
): Cursor {
    val decoded = CombinedDecodedPageCursor(
        request = request,
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info,
                dataSourceId = page.dataSourceId
            )
        }
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = serializer
    )
}

/**
 * Encodes the provided values as a [CombinedDecodedPageCursor] model in its encoded form, using
 * the provided [format].
 *
 * @param [request] The [PageRequest] made to the underlying [PagedDataSource]s.
 *
 * @param [pages] The [ResolvedPage]s returned by the [PagedDataSource]s.
 *
 * @param [format] The [BinaryFormat] used to encode the constructed [CombinedDecodedPageCursor]
 * model into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [CombinedDecodedPageCursor]
 * @see [Cursor.Companion.encode]
 * @see [CombinedPagedDataSource]
 */
@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any, Item> Cursor.Companion.encode(
    request: PageRequest<Data, Filters>,
    pages: List<ResolvedPage<Item>>,
    format: BinaryFormat,
    serializer: SerializationStrategy<CombinedDecodedPageCursor<Data, Filters>> = format.serializersModule.serializer()
): Cursor {
    val decoded = CombinedDecodedPageCursor(
        request = request,
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info,
                dataSourceId = page.dataSourceId
            )
        }
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = serializer
    )
}

/**
 * Encodes the provided values as a [CombinedDecodedPageCursor] model in its encoded form, using
 * the provided [format].
 *
 * @param [request] The [PageRequest] made to the underlying [PagedDataSource]s.
 *
 * @param [pages] The [ResolvedPage]s returned by the [PagedDataSource]s.
 *
 * @param [format] The [StringFormat] used to encode the constructed [CombinedDecodedPageCursor]
 * model into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [CombinedDecodedPageCursor]
 * @see [Cursor.Companion.encode]
 * @see [CombinedPagedDataSource]
 */
@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Cursor.Companion.encode(
    request: PageRequest<Data, Filters>,
    pages: List<ResolvedPage<Item>>,
    format: StringFormat = Json.Default,
    dataSerializer: KSerializer<Data>,
    filtersSerializer: KSerializer<Filters>
): Cursor {
    val decoded = CombinedDecodedPageCursor(
        request = request,
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info,
                dataSourceId = page.dataSourceId
            )
        }
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = CombinedDecodedPageCursor.serializer(dataSerializer, filtersSerializer)
    )
}

/**
 * Encodes the provided values as a [CombinedDecodedPageCursor] model in its encoded form, using
 * the provided [format].
 *
 * @param [request] The [PageRequest] made to the underlying [PagedDataSource]s.
 *
 * @param [pages] The [ResolvedPage]s returned by the [PagedDataSource]s.
 *
 * @param [format] The [BinaryFormat] used to encode the constructed [CombinedDecodedPageCursor]
 * model into a [Cursor] instance.
 *
 * @throws [SerializationException] in case of any encoding-specific error.
 *
 * @throws [IllegalArgumentException] if the encoded input does not comply format's specification.
 *
 * @return A [Cursor] with the provided values encoded.
 *
 * @see [CombinedDecodedPageCursor]
 * @see [Cursor.Companion.encode]
 * @see [CombinedPagedDataSource]
 */
@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Cursor.Companion.encode(
    request: PageRequest<Data, Filters>,
    pages: List<ResolvedPage<Item>>,
    format: BinaryFormat,
    dataSerializer: KSerializer<Data>,
    filtersSerializer: KSerializer<Filters>
): Cursor {
    val decoded = CombinedDecodedPageCursor(
        request = request,
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info,
                dataSourceId = page.dataSourceId
            )
        }
    )

    return Cursor.encode(
        value = decoded,
        format = format,
        serializer = CombinedDecodedPageCursor.serializer(dataSerializer, filtersSerializer)
    )
}

@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any> Cursor.decodeCombined(
    format: StringFormat = Json.Default,
    deserializer: DeserializationStrategy<CombinedDecodedPageCursor<Data, Filters>> = format.serializersModule.serializer()
): CombinedDecodedPageCursor<Data, Filters> = this.decode(
    format = format,
    deserializer = deserializer
)

@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any> Cursor.decodeCombined(
    format: StringFormat = Json.Default,
    dataSerializer: KSerializer<Data>,
    filtersSerializer: KSerializer<Filters>
): CombinedDecodedPageCursor<Data, Filters> = this.decode(
    format = format,
    deserializer = CombinedDecodedPageCursor.serializer(dataSerializer, filtersSerializer)
)

@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any> Cursor.decodeCombinedOrNull(
    format: StringFormat = Json.Default,
    deserializer: DeserializationStrategy<CombinedDecodedPageCursor<Data, Filters>> = format.serializersModule.serializer()
): CombinedDecodedPageCursor<Data, Filters>? = this.decodeOrNull(
    format = format,
    deserializer = deserializer
)

@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any> Cursor.decodeCombinedOrNull(
    format: StringFormat = Json.Default,
    dataSerializer: KSerializer<Data>,
    filtersSerializer: KSerializer<Filters>
): CombinedDecodedPageCursor<Data, Filters>? = this.decodeOrNull(
    format = format,
    deserializer = CombinedDecodedPageCursor.serializer(dataSerializer, filtersSerializer)
)
