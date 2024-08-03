package com.mooncloak.kodetools.storagex.keyvalue.redis

import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.invoke
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    override val format: StringFormat
) : RedisKeyValueStorage {

    private val mutex = Mutex(locked = false)

    private var connection: StatefulRedisConnection<String, String> = client.connect()
    private var cachedCommands = connection.async()

    override suspend fun size(): Long =
        commands().dbsize().await()

    override suspend fun containsKey(key: String): Boolean =
        commands().get(key).await() != null

    override suspend fun getValue(key: String): KeyValueStorage.StoredValue<*> {
        val rawValue: String = commands().get(key).await()
            ?: throw NoSuchElementException("No entry found with key '$key'.")

        return KeyValueStorage.StoredValue(
            rawValue = rawValue,
            format = format
        )
    }

    override suspend fun entries(): Set<KeyValueStorage.Entry<String, *>> {
        throw UnsupportedOperationException("entries() is not supported for the RedisKeyValueStorage.")
    }

    override suspend fun <T : Any> put(
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

    override suspend fun <Value : Any> put(
        key: String,
        serializer: SerializationStrategy<Value>,
        value: Value?
    ): KeyValueStorage.StoredValue<*>? =
        mutex.withLock {
            val previousRawValue: String? = commands().get(key).await()
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            if (value == null) {
                commands().del(key).await()
            } else {
                val rawValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                commands().set(key, rawValue).await()
            }

            previousStoredValue
        }

    override suspend fun <Value> putAll(entries: Collection<MutableKeyValueStorage.InputEntry<String, Value>>) {
        mutex.withLock {
            coroutineScope {
                entries.map { entry ->
                    async {
                        val value = entry.inputValue.value

                        if (value == null) {
                            commands().del(entry.key).await()
                        } else {
                            val rawValue = format.encodeToString(
                                serializer = entry.inputValue.serializer,
                                value = value
                            )

                            commands().set(entry.key, rawValue).await()
                        }
                    }
                }.awaitAll()
            }
        }
    }

    override suspend fun remove(key: String): KeyValueStorage.StoredValue<*>? {
        mutex.withLock {
            val previousRawValue: String? = commands().get(key).await()
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            commands().del(key).await()

            return previousStoredValue
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
