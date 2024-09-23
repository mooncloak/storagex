//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[changesOrNull](changes-or-null.md)

# changesOrNull

[common]\
fun &lt;[Key](changes-or-null.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage](-key-value-storage/index.md)&lt;[Key](changes-or-null.md)&gt;.[changesOrNull](changes-or-null.md)(key: [Key](changes-or-null.md)): Flow&lt;[KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;*&gt;?&gt;?

Retrieves a Flow of value changes for the provided [key](changes-or-null.md). Any time the value changes for the [key](changes-or-null.md), the new value will be emitted on the returned Flow instance.

#### Return

A Flow of [StoredValue](-key-value-storage/-stored-value/index.md)s associated with the provided [key](changes-or-null.md) and that are emitted every time the value associated with the provided [key](changes-or-null.md) changes, or `null` if this operation is not supported by the implementation of this [KeyValueStorage](-key-value-storage/index.md) instance.

#### Parameters

common

| | |
|---|---|
| key | The [Key](changes-or-null.md) whose value changes are to be observed. |

#### See also

| |
|---|
| [KeyValueStorage.changes](-key-value-storage/changes.md) |
