//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[KeyValueStorage](index.md)

# KeyValueStorage

interface [KeyValueStorage](index.md)&lt;[Key](index.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt;

Represents a generic storage of data for key-value pairs. This abstraction is used, instead of a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) or similar interface, because each of the functions are suspending, allowing implementations to have any data source and for them to use a Mutex to guarantee concurrency safety.

!Note Implementations of this interface SHOULD guarantee to be concurrency-safe.

#### Inheritors

| |
|---|
| [MutableKeyValueStorage](../-mutable-key-value-storage/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |
| [Entry](-entry/index.md) | [common]<br>interface [Entry](-entry/index.md)&lt;[Key](-entry/index.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [RawValue](-entry/index.md)&gt; |
| [StoredValue](-stored-value/index.md) | [common]<br>interface [StoredValue](-stored-value/index.md)&lt;[RawValue](-stored-value/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [format](format.md) | [common]<br>abstract val [format](format.md): SerialFormat<br>The SerialFormat that is used to serialize and deserialize the values for the underlying storage. |

## Functions

| Name | Summary |
|---|---|
| [changes](changes.md) | [common]<br>open fun [changes](changes.md)(key: [Key](index.md)): Flow&lt;[KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;?&gt;<br>Retrieves a Flow of value changes for the provided [key](changes.md). Any time the value changes for the [key](changes.md), the new value will be emitted on the returned Flow instance. |
| [changesOrNull](../changes-or-null.md) | [common]<br>fun &lt;[Key](../changes-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage](index.md)&lt;[Key](../changes-or-null.md)&gt;.[changesOrNull](../changes-or-null.md)(key: [Key](../changes-or-null.md)): Flow&lt;[KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;?&gt;?<br>Retrieves a Flow of value changes for the provided [key](../changes-or-null.md). Any time the value changes for the [key](../changes-or-null.md), the new value will be emitted on the returned Flow instance. |
| [close](close.md) | [common]<br>open suspend fun [close](close.md)()<br>Closes the connection to the underlying data structure and clears any system resources. |
| [containsKey](contains-key.md) | [common]<br>open suspend fun [containsKey](contains-key.md)(key: [Key](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if the [KeyValueStorage](index.md) contains the specified key, `false` otherwise. |
| [entries](entries.md) | [common]<br>abstract suspend fun [entries](entries.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[KeyValueStorage.Entry](-entry/index.md)&lt;[Key](index.md), *&gt;&gt;<br>Returns a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) of all [Entry](-entry/index.md) items in this [KeyValueStorage](index.md). |
| [get](get.md) | [common]<br>open suspend fun &lt;[T](get.md)&gt; [get](get.md)(key: [Key](index.md), deserializer: DeserializationStrategy&lt;[T](get.md)&gt;): [T](get.md)<br>Retrieves the value for the provided [key](get.md), using the provided [deserializer](get.md) to convert the value from its internal representation to the model of type [T](get.md), or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is no value for the provided [key](get.md). |
| [get](../get.md) | [common]<br>inline suspend fun &lt;[Key](../get.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](../get.md)&gt; [KeyValueStorage](index.md)&lt;[Key](../get.md)&gt;.[get](../get.md)(key: [Key](../get.md)): [Value](../get.md)<br>A convenience function for invoking the [KeyValueStorage.get](get.md) function with a DeserializationStrategy obtained via the serializer function. |
| [getOrNull](../get-or-null.md) | [common]<br>inline suspend fun &lt;[Key](../get-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](../get-or-null.md)&gt; [KeyValueStorage](index.md)&lt;[Key](../get-or-null.md)&gt;.[getOrNull](../get-or-null.md)(key: [Key](../get-or-null.md), deserializer: KSerializer&lt;[Value](../get-or-null.md)&gt; = this.format.serializersModule.serializer()): [Value](../get-or-null.md)?<br>Retrieves the value for the provided [key](../get-or-null.md), using the provided [deserializer](../get-or-null.md) to convert the value from its internal representation to the model of type [Value](../get-or-null.md), or `null` if there is no value for the provided [key](../get-or-null.md) or it has expired. |
| [getValue](get-value.md) | [common]<br>abstract suspend fun [getValue](get-value.md)(key: [Key](index.md)): [KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;<br>Retrieves the [StoredValue](-stored-value/index.md) for the provided [key](get-value.md), or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is no value for the provided [key](get-value.md). |
| [getValueOrNull](../get-value-or-null.md) | [common]<br>suspend fun &lt;[Key](../get-value-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage](index.md)&lt;[Key](../get-value-or-null.md)&gt;.[getValueOrNull](../get-value-or-null.md)(key: [Key](../get-value-or-null.md)): [KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;?<br>Retrieves the [StoredValue](-stored-value/index.md) for the provided [key](../get-value-or-null.md), or `null` if there is no value for the provided [key](../get-value-or-null.md). |
| [isEmpty](is-empty.md) | [common]<br>open suspend fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if the [KeyValueStorage](index.md) is empty, `false` otherwise. |
| [isNotEmpty](../is-not-empty.md) | [common]<br>suspend fun &lt;[Key](../is-not-empty.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage](index.md)&lt;[Key](../is-not-empty.md)&gt;.[isNotEmpty](../is-not-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns `true` if the [KeyValueStorage](index.md) is NOT empty, `false` otherwise. This is the inverse of the [KeyValueStorage.isEmpty](is-empty.md) function, provided for convenience. |
| [keys](keys.md) | [common]<br>open suspend fun [keys](keys.md)(): [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)&lt;[Key](index.md)&gt;<br>Returns a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) of all [Key](index.md)s in this [KeyValueStorage](index.md). |
| [size](size.md) | [common]<br>abstract suspend fun [size](size.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>Retrieves the amount of key-value pairs stored in the [KeyValueStorage](index.md). |
| [values](values.md) | [common]<br>open suspend fun [values](values.md)(): [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;&gt;<br>Returns a [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html) of all [StoredValue](-stored-value/index.md)s in this [KeyValueStorage](index.md). Note that the returned [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html) may return duplicate values. |
