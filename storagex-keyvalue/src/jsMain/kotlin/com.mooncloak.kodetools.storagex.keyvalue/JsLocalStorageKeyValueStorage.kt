package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set

public open class LocalStorageKeyValueStorage public constructor(
    private val format: StringFormat,
    private val localStorage: Storage
) : MutableKeyValueStorage<String> {

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Int =
        localStorage.length

    override suspend fun contains(key: String): Boolean =
        localStorage[key] != null

    override suspend fun <Value : Any> get(key: String, deserializer: KSerializer<Value>): Value? {
        val rawValue = localStorage[key] ?: return null

        return format.decodeFromString(deserializer = deserializer, string = rawValue)
    }

    override fun <Value : Any> flow(key: String, deserializer: KSerializer<Value>): Flow<Value?> {
        throw UnsupportedOperationException("The flow function is not supported by LocalStorageKeyValueStorage.")
    }

    override suspend fun remove(key: String) {
        mutex.withLock {
            localStorage.removeItem(key = key)
        }
    }

    override suspend fun <Value : Any> set(key: String, value: Value?, serializer: KSerializer<Value>) {
        mutex.withLock {
            if (value == null) {
                localStorage.removeItem(key = key)
            } else {
                val rawValue = format.encodeToString(serializer = serializer, value = value)

                localStorage.set(key = key, value = rawValue)
            }
        }
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
