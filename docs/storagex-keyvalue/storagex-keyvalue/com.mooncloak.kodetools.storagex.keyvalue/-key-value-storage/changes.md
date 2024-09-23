//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[KeyValueStorage](index.md)/[changes](changes.md)

# changes

[common]\
open fun [changes](changes.md)(key: [Key](index.md)): Flow&lt;[KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;?&gt;

Retrieves a Flow of value changes for the provided [key](changes.md). Any time the value changes for the [key](changes.md), the new value will be emitted on the returned Flow instance.

#### Return

A Flow of [StoredValue](-stored-value/index.md)s associated with the provided [key](changes.md) and that are emitted every time the value associated with the provided [key](changes.md) changes.

#### Parameters

common

| | |
|---|---|
| key | The [Key](index.md) whose value changes are to be observed. |

#### See also

| |
|---|
| [changesOrNull](../changes-or-null.md) |

#### Throws

| | |
|---|---|
| [UnsupportedOperationException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unsupported-operation-exception/index.html) | if the implementation does not support this functionality. |
