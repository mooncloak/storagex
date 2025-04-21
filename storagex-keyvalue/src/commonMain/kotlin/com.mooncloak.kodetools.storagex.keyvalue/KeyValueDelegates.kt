package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Creates a read-only property delegate backed by this [MutableKeyValueStorage].
 *
 * This function simplifies the creation of [KeyValueProperty] values that are automatically persisted to the
 * underlying key-value storage. It uses Kotlin's property delegation mechanism for convenient access and modification.
 */
public inline fun <reified Value : Any> MutableKeyValueStorage<String>.property(
    key: String? = null,
    serializer: KSerializer<Value> = serializer()
): ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> =
    StringKeyValuePropertyDelegate(
        key = key,
        serializer = serializer,
        storage = this
    )

/**
 * Creates a read-only property delegate backed by this [MutableKeyValueStorage].
 *
 * This function simplifies the creation of [KeyValueProperty] values that are automatically persisted to the
 * underlying key-value storage. It uses Kotlin's property delegation mechanism for convenient access and modification.
 */
public inline fun <reified Key : Any, reified Value : Any> MutableKeyValueStorage<Key>.property(
    key: Key,
    serializer: KSerializer<Value> = serializer()
): ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> =
    KeyValuePropertyDelegate(
        key = key,
        serializer = serializer,
        storage = this
    )

@PublishedApi
internal class StringKeyValuePropertyDelegate<Value : Any> @PublishedApi internal constructor(
    private val key: String?,
    private val serializer: KSerializer<Value>,
    private val storage: MutableKeyValueStorage<String>
) : ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableKeyValueProperty<Value> =
        DefaultKeyValueProperty(
            key = key ?: property.name,
            serializer = serializer,
            storage = storage
        )
}

@PublishedApi
internal class KeyValuePropertyDelegate<Key : Any, Value : Any> @PublishedApi internal constructor(
    private val key: Key,
    private val serializer: KSerializer<Value>,
    private val storage: MutableKeyValueStorage<Key>
) : ReadOnlyProperty<Any, MutableKeyValueProperty<Value>> {

    override fun getValue(thisRef: Any, property: KProperty<*>): MutableKeyValueProperty<Value> =
        DefaultKeyValueProperty(
            key = key,
            serializer = serializer,
            storage = storage
        )
}
