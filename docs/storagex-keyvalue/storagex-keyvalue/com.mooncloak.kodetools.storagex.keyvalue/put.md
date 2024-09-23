//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[put](put.md)

# put

[common]\
inline suspend fun &lt;[Key](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [MutableKeyValueStorage](-mutable-key-value-storage/index.md)&lt;[Key](put.md)&gt;.[put](put.md)(key: [Key](put.md), value: [Value](put.md)?): [KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;*&gt;?

Sets the value for the provided [key](put.md) to be equal to the provided [value](put.md), using the provided serializer to convert the model of type [Value](put.md) to its internal representation. This is a convenience function for invoking the [MutableKeyValueStorage.put](-mutable-key-value-storage/put.md) function with a SerializationStrategy obtained via the serializer function.

#### Return

Returns the previous [StoredValue](-key-value-storage/-stored-value/index.md) associated with the [key](put.md), or `null` if the key was not present in the [KeyValueStorage](-key-value-storage/index.md).

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being set. This same value can be used to retrieve the data later. |
| value | The value to set for the [key](put.md). This value can be `null` to clear the value. |

#### See also

| |
|---|
| [MutableKeyValueStorage.put](-mutable-key-value-storage/put.md) |
