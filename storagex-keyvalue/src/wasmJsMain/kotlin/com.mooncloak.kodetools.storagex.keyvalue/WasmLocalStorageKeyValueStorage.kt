package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set
import kotlin.reflect.KClass

@Suppress("FunctionName")
public fun KeyValueStorage.Companion.LocalStorage(
    format: StringFormat,
    localStorage: Storage
): KeyValueStorage = LocalStorageKeyValueStorage(
    format = format,
    localStorage = localStorage
)

internal class LocalStorageKeyValueStorage internal constructor(
    private val format: StringFormat,
    private val localStorage: Storage
) : KeyValueStorage {

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Long =
        localStorage.length.toLong()

    override suspend fun contains(key: String): Boolean =
        localStorage[key] != null

    override suspend fun <T : Any> get(
        key: String,
        deserializer: DeserializationStrategy<T>,
        kClass: KClass<T>
    ): T {
        val encodedValue =
            localStorage.getItem(key = key)
                ?: throw NoSuchElementException("No stored value with key = $key")

        return format.decodeFromString(
            deserializer = deserializer,
            string = encodedValue
        )
    }

    override suspend fun <T : Any> set(
        key: String,
        serializer: SerializationStrategy<T>,
        kClass: KClass<T>,
        value: T?
    ): Unit =
        mutex.withLock {
            if (value == null) {
                localStorage.removeItem(key = key)
            } else {
                val encodedValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                localStorage.set(
                    key = key,
                    value = encodedValue
                )
            }
        }

    override suspend fun remove(key: String): Unit =
        mutex.withLock {
            localStorage.removeItem(key = key)
        }

    override suspend fun clear(): Unit =
        mutex.withLock {
            localStorage.clear()
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LocalStorageKeyValueStorage) return false

        if (format != other.format) return false
        if (localStorage != other.localStorage) return false
        if (mutex != other.mutex) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + localStorage.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String =
        "LocalStorageKeyValueStorage(format=$format, localStorage=$localStorage, mutex=$mutex)"
}
