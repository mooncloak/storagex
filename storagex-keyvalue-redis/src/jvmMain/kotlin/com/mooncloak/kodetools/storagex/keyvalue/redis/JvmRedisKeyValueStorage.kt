package com.mooncloak.kodetools.storagex.keyvalue.redis

import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlin.reflect.KClass
import kotlin.time.Duration

/**
 * Creates a [KeyValueStorage] instance that uses the Redis service defined by the provided
 * [RedisClient]. Note that this storage only works with [String] values, and is serialized and
 * deserialized to [String] values locally. If a value is stored in another format, and attempted
 * to be retrieved via this component, the behavior is undefined.
 *
 * @param [client] The [RedisClient] used to connect to the Redis service instance.
 *
 * @param [format] The [StringFormat] used to serialize and deserialize the stored [String] values.
 */
@Suppress("FunctionName")
public fun KeyValueStorage.Companion.Redis(
    client: RedisClient,
    format: StringFormat
): RedisKeyValueStorage = RedisKeyValueStorageImpl(
    client = client,
    format = format
)

internal class RedisKeyValueStorageImpl internal constructor(
    private val client: RedisClient,
    private val format: StringFormat
) : RedisKeyValueStorage {

    private val mutex = Mutex(locked = false)

    private var connection: StatefulRedisConnection<String, String> = client.connect()
    private var cachedCommands = connection.async()

    override suspend fun count(): Int =
        commands().dbsize().await().toInt()

    override suspend fun contains(key: String): Boolean =
        commands().get(key).await() != null

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        val rawValue: String = commands().get(key).await() ?: return null

        return format.decodeFromString(deserializer = deserializer, string = rawValue)
    }

    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        throw UnsupportedOperationException("flow function in JvmRedisKeyValueStorage is not supported.")
    }

    override suspend fun <T : Any> set(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?,
        expiresIn: Duration
    ) {
        mutex.withLock {
            if (value == null) {
                commands().del(key).await()
            } else {
                val stringValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                commands().set(key, stringValue).await()
                commands().expire(key, expiresIn.inWholeSeconds)
            }
        }
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        mutex.withLock {
            if (value == null) {
                commands().del(key).await()
            } else {
                val rawValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                commands().set(key, rawValue).await()
            }
        }
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            commands().del(key).await()
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            commands().flushdb().await()
        }
    }

    override suspend fun close() {
        mutex.withLock {
            connection.closeAsync().await()
        }
    }

    private fun commands(): RedisAsyncCommands<String, String> {
        if (!connection.isOpen) {
            connection = client.connect()
            cachedCommands = connection.async()
        }

        return cachedCommands
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RedisKeyValueStorageImpl) return false

        if (client != other.client) return false
        if (format != other.format) return false
        if (mutex != other.mutex) return false
        if (connection != other.connection) return false
        if (cachedCommands != other.cachedCommands) return false

        return cachedCommands == other.cachedCommands
    }

    override fun hashCode(): Int {
        var result = client.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + mutex.hashCode()
        result = 31 * result + connection.hashCode()
        result = 31 * result + (cachedCommands?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "RedisKeyValueStorageImpl(client=$client, format=$format, mutex=$mutex, connection=$connection, cachedCommands=$cachedCommands)"
}
