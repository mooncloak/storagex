package com.mooncloak.kodetools.storagex.keyvalue.cache

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

public actual fun com.mooncloak.kodetools.storagex.keyvalue.cache.Cache.Companion.create(
    format: StringFormat,
    maxSize: Int?,
    expirationAfterWrite: Duration?,
    clock: Clock
): com.mooncloak.kodetools.storagex.keyvalue.cache.Cache<String> {
    var cacheBuilder = Caffeine.newBuilder()

    if (maxSize != null) {
        cacheBuilder = cacheBuilder.maximumSize(maxSize.toLong())
    }

    if (expirationAfterWrite != null) {
        cacheBuilder = cacheBuilder.expireAfterWrite(expirationAfterWrite.inWholeMilliseconds, TimeUnit.MILLISECONDS)
    }

    return JvmCache(
        format = format,
        cache = cacheBuilder.build()
    )
}

internal class JvmCache internal constructor(
    private val format: StringFormat,
    private val cache: Cache<String, String>
) : com.mooncloak.kodetools.storagex.keyvalue.cache.Cache<String> {

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Int = cache.estimatedSize().toInt()

    override suspend fun contains(key: String): Boolean =
        cache.getIfPresent(key) != null

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        val stored = cache.getIfPresent(key) ?: return null

        return format.decodeFromString(
            deserializer = deserializer,
            string = stored
        )
    }

    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        throw UnsupportedOperationException("flow function is not supported by JvmCache implementation.")
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        mutex.withLock {
            if (value == null) {
                cache.invalidate(key)
            } else {
                val stored = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                cache.put(key, stored)
            }
        }
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            cache.invalidate(key)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            cache.invalidateAll()
        }
    }
}
