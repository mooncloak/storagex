package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import org.w3c.dom.Storage
import org.w3c.dom.get
import org.w3c.dom.set

@Suppress("FunctionName")
public fun KeyValueStorage.Companion.LocalStorage(
    format: StringFormat,
    localStorage: Storage
): KeyValueStorage<String> = LocalStorageKeyValueStorage(
    format = format,
    localStorage = localStorage
)

internal class LocalStorageKeyValueStorage internal constructor(
    override val format: StringFormat,
    private val localStorage: Storage
) : MutableKeyValueStorage<String> {

    private val mutex = Mutex(locked = false)

    override suspend fun size(): Long =
        localStorage.length.toLong()

    override suspend fun entries(): Set<KeyValueStorage.Entry<String, *>> =
        localStorageKeys().mapNotNull { key ->
            val rawValue = localStorage[key]

            rawValue?.let {
                KeyValueStorage.Entry(
                    key = key,
                    rawValue = it,
                    format = format
                )
            }
        }.toSet()

    override suspend fun getValue(key: String): KeyValueStorage.StoredValue<*> {
        val rawValue = localStorage[key]
            ?: throw NoSuchElementException("No entry found with key '$key'.")

        return KeyValueStorage.StoredValue(
            rawValue = rawValue,
            format = format
        )
    }

    override suspend fun remove(key: String): KeyValueStorage.StoredValue<String>? =
        mutex.withLock {
            val previousRawValue = localStorage[key]
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            localStorage.removeItem(key = key)

            previousStoredValue
        }

    override suspend fun <Value : Any> put(
        key: String,
        serializer: SerializationStrategy<Value>,
        value: Value?
    ): KeyValueStorage.StoredValue<*>? =
        mutex.withLock {
            val previousRawValue = localStorage[key]
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            if (value == null) {
                localStorage.removeItem(key)
            } else {
                val rawValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                localStorage[key] = rawValue
            }

            previousStoredValue
        }

    override suspend fun <Value> putAll(entries: Collection<MutableKeyValueStorage.InputEntry<String, Value>>) {
        mutex.withLock {
            entries.forEach { entry ->
                val value = entry.inputValue.value

                if (value == null) {
                    localStorage.removeItem(entry.key)
                } else {
                    val rawValue = format.encodeToString(
                        serializer = entry.inputValue.serializer,
                        value = value
                    )

                    localStorage[entry.key] = rawValue
                }
            }
        }
    }

    override suspend fun clear(): Unit =
        mutex.withLock {
            localStorage.clear()
        }

    private fun localStorageKeys(): Set<String> {
        val result = mutableSetOf<String>()

        for (i in 0 until localStorage.length) {
            localStorage.key(i)?.let { result.add(it) }
        }

        return result
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
