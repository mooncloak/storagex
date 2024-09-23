//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[MutableKeyValueStorage](index.md)/[put](put.md)

# put

[common]\
abstract suspend fun &lt;[Value](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [put](put.md)(key: [Key](index.md), serializer: SerializationStrategy&lt;[Value](put.md)&gt;, value: [Value](put.md)?): [KeyValueStorage.StoredValue](../-key-value-storage/-stored-value/index.md)&lt;*&gt;?

Sets the value for the provided [key](put.md) to be equal to the provided [value](put.md), using the provided [serializer](put.md) to convert the model of type [Value](put.md) to its internal representation.

#### Return

Returns the previous [StoredValue](../-key-value-storage/-stored-value/index.md) associated with the [key](put.md), or `null` if the key was not present in the [KeyValueStorage](../-key-value-storage/index.md).

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being set. This same value can be used to retrieve the data later. |
| serializer | The SerializationStrategy that is used to convert the data into its internal representation. This value must correspond to the DeserializationStrategy that is used when retrieving the data, otherwise an exception will be thrown. |
| value | The value to set for the [key](put.md). This value can be `null` to clear the value. |
