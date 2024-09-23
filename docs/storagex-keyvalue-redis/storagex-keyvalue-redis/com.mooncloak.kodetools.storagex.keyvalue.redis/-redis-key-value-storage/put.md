//[storagex-keyvalue-redis](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue.redis](../index.md)/[RedisKeyValueStorage](index.md)/[put](put.md)

# put

[common]\
abstract suspend fun &lt;[T](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [put](put.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), serializer: SerializationStrategy&lt;[T](put.md)&gt;, kClass: [KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;[T](put.md)&gt;, value: [T](put.md)?, expiresIn: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html))

Sets the value for the provided [key](put.md) to be equal to the provided [value](put.md), using the provided [serializer](put.md) to convert the model of type [T](put.md) to its internal representation.

#### Parameters

common

| | |
|---|---|
| key | The [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) key value that uniquely identifies the data that is being set. This same value can be used to retrieve the data later. |
| serializer | The SerializationStrategy that is used to convert the data into its internal representation. This value must correspond to the DeserializationStrategy that is used when retrieving the data, otherwise an exception will be thrown. |
| value | The value to set for the [key](put.md). This value can be `null` to clear the cached value. |
