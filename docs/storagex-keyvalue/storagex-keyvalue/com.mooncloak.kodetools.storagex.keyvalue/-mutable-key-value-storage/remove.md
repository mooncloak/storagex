//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[MutableKeyValueStorage](index.md)/[remove](remove.md)

# remove

[common]\
abstract suspend fun [remove](remove.md)(key: [Key](index.md)): [KeyValueStorage.StoredValue](../-key-value-storage/-stored-value/index.md)&lt;*&gt;?

Removes the entry with the provided [key](remove.md).

#### Return

Returns the previous [StoredValue](../-key-value-storage/-stored-value/index.md) associated with the [key](remove.md), or `null` if the key was not present in the [KeyValueStorage](../-key-value-storage/index.md).
