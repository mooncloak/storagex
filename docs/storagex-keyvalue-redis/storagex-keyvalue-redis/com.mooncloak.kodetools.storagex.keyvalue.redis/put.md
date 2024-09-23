//[storagex-keyvalue-redis](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue.redis](index.md)/[put](put.md)

# put

[common]\
inline suspend fun &lt;[T](put.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [RedisKeyValueStorage](-redis-key-value-storage/index.md).[put](put.md)(key: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [T](put.md)?, expiresIn: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html))

A convenience function for invoking the KeyValueStorage.put function with a SerializationStrategy obtained via the serializer function.

#### See also

| |
|---|
| [MutableKeyValueStorage.put](../../../storagex-keyvalue/storagex-keyvalue/com.mooncloak.kodetools.storagex.keyvalue/-mutable-key-value-storage/put.md) |
