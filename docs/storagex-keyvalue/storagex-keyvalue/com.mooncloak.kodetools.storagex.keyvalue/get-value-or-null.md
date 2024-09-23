//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[getValueOrNull](get-value-or-null.md)

# getValueOrNull

[common]\
suspend fun &lt;[Key](get-value-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage](-key-value-storage/index.md)&lt;[Key](get-value-or-null.md)&gt;.[getValueOrNull](get-value-or-null.md)(key: [Key](get-value-or-null.md)): [KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;*&gt;?

Retrieves the [StoredValue](-key-value-storage/-stored-value/index.md) for the provided [key](get-value-or-null.md), or `null` if there is no value for the provided [key](get-value-or-null.md).

#### Return

The [StoredValue](-key-value-storage/-stored-value/index.md) associated with the provided [key](get-value-or-null.md), or `null` if there is none.

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being retrieved. |
