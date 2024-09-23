//[storagex-keyvalue-redis](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue.redis](index.md)/[Redis](-redis.md)

# Redis

[jvm]\
fun [KeyValueStorage.Companion](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-key-value-storage/-companion/index.md).[Redis](-redis.md)(client: RedisClient, format: StringFormat): [RedisKeyValueStorage](-redis-key-value-storage/index.md)

Creates a [KeyValueStorage](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-key-value-storage/index.md) instance that uses the Redis service defined by the provided RedisClient. Note that this storage only works with [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values, and is serialized and deserialized to [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values locally. If a value is stored in another format, and attempted to be retrieved via this component, the behavior is undefined.

#### Parameters

jvm

| | |
|---|---|
| client | The RedisClient used to connect to the Redis service instance. |
| format | The StringFormat used to serialize and deserialize the stored [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) values. |
