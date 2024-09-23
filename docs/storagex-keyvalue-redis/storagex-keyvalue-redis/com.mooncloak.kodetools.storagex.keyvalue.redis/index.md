//[storagex-keyvalue-redis](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue.redis](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [RedisConfig](-redis-config/index.md) | [common]<br>@Serializable<br>data class [RedisConfig](-redis-config/index.md)(val username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val password: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [RedisKeyValueStorage](-redis-key-value-storage/index.md) | [common]<br>interface [RedisKeyValueStorage](-redis-key-value-storage/index.md) : [MutableKeyValueStorage](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-mutable-key-value-storage/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; |

## Properties

| Name | Summary |
|---|---|
| [secureConnectionString](secure-connection-string.md) | [common]<br>val [RedisConfig](-redis-config/index.md).[secureConnectionString](secure-connection-string.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [put](put.md) | [common]<br>inline suspend fun &lt;[T](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [RedisKeyValueStorage](-redis-key-value-storage/index.md).[put](put.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [T](put.md)?, expiresIn: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html))<br>A convenience function for invoking the KeyValueStorage.put function with a SerializationStrategy obtained via the serializer function. |
| [Redis](-redis.md) | [jvm]<br>fun [KeyValueStorage.Companion](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-key-value-storage/-companion/index.md).[Redis](-redis.md)(client: RedisClient, format: StringFormat): [RedisKeyValueStorage](-redis-key-value-storage/index.md)<br>Creates a [KeyValueStorage](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-key-value-storage/index.md) instance that uses the Redis service defined by the provided RedisClient. Note that this storage only works with [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values, and is serialized and deserialized to [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values locally. If a value is stored in another format, and attempted to be retrieved via this component, the behavior is undefined. |
