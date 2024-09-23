//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[KeyValueStorage](index.md)/[getValue](get-value.md)

# getValue

[common]\
abstract suspend fun [getValue](get-value.md)(key: [Key](index.md)): [KeyValueStorage.StoredValue](-stored-value/index.md)&lt;*&gt;

Retrieves the [StoredValue](-stored-value/index.md) for the provided [key](get-value.md), or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is no value for the provided [key](get-value.md).

#### Return

The [StoredValue](-stored-value/index.md) associated with the provided [key](get-value.md), or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is none.

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being retrieved. |

#### Throws

| | |
|---|---|
| [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) | If there is no element in the underlying storage with the provided [key](get-value.md). |
