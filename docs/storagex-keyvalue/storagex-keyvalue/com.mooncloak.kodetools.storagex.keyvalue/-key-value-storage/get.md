//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[KeyValueStorage](index.md)/[get](get.md)

# get

[common]\
open suspend fun &lt;[T](get.md)&gt; [get](get.md)(key: [Key](index.md), deserializer: DeserializationStrategy&lt;[T](get.md)&gt;): [T](get.md)

Retrieves the value for the provided [key](get.md), using the provided [deserializer](get.md) to convert the value from its internal representation to the model of type [T](get.md), or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is no value for the provided [key](get.md).

#### Return

[T](get.md) The value, or throws [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) if there is none.

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being retrieved. |
| deserializer | The DeserializationStrategy that is used to convert the internal representation of the data into the model of type [T](get.md). If the wrong [deserializer](get.md) is used, an exception will be thrown. |

#### Throws

| | |
|---|---|
| [NoSuchElementException](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/index.html) | If there is no element in the underlying storage with the provided [key](get.md). |
