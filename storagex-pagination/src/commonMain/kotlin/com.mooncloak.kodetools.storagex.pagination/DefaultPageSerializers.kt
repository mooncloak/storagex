package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@ExperimentalPaginationAPI
public class ResolvedPageSerializer<Item> public constructor(
    itemSerializer: KSerializer<Item>
) : KSerializer<ResolvedPage<Item>> {

    private val delegateSerializer = DefaultResolvedPage.serializer(itemSerializer)

    override val descriptor: SerialDescriptor =
        delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): ResolvedPage<Item> =
        decoder.decodeSerializableValue(
            deserializer = delegateSerializer
        )

    override fun serialize(encoder: Encoder, value: ResolvedPage<Item>) {
        when (value) {
            is DefaultResolvedPage<Item> -> encoder.encodeSerializableValue(
                serializer = delegateSerializer,
                value = value
            )

            else -> encoder.encodeSerializableValue(
                serializer = delegateSerializer,
                value = DefaultResolvedPage(
                    id = value.id,
                    dataSourceId = value.dataSourceId,
                    pageCursor = value.pageCursor,
                    items = value.items
                )
            )
        }
    }
}

@ExperimentalPaginationAPI
public class PageCollectionSerializer<Item> public constructor(
    itemSerializer: KSerializer<Item>
) : KSerializer<PageCollection<Item>> {

    private val delegateSerializer = DefaultPageCollection.serializer(itemSerializer)

    override val descriptor: SerialDescriptor =
        delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): PageCollection<Item> =
        decoder.decodeSerializableValue(
            deserializer = delegateSerializer
        )

    override fun serialize(encoder: Encoder, value: PageCollection<Item>) {
        when (value) {
            is DefaultPageCollection<Item> -> encoder.encodeSerializableValue(
                serializer = delegateSerializer,
                value = value
            )

            else -> encoder.encodeSerializableValue(
                serializer = delegateSerializer,
                value = DefaultPageCollection(
                    id = value.id,
                    pageCursor = value.pageCursor,
                    dataSourceId = value.dataSourceId,
                    pages = value.pages
                )
            )
        }
    }
}
