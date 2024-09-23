//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[getOrNull](get-or-null.md)

# getOrNull

[common]\
inline suspend fun &lt;[Key](get-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](get-or-null.md)&gt; [KeyValueStorage](-key-value-storage/index.md)&lt;[Key](get-or-null.md)&gt;.[getOrNull](get-or-null.md)(key: [Key](get-or-null.md), deserializer: KSerializer&lt;[Value](get-or-null.md)&gt; = this.format.serializersModule.serializer()): [Value](get-or-null.md)?

Retrieves the value for the provided [key](get-or-null.md), using the provided [deserializer](get-or-null.md) to convert the value from its internal representation to the model of type [Value](get-or-null.md), or `null` if there is no value for the provided [key](get-or-null.md) or it has expired.

#### Return

[Value](get-or-null.md) The cached value, or `null` if there is none, or it expired.

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being retrieved. |
| deserializer | The DeserializationStrategy that is used to convert the internal representation of the data into the model of type [Value](get-or-null.md). If the wrong [deserializer](get-or-null.md) is used, an exception will be thrown. |
