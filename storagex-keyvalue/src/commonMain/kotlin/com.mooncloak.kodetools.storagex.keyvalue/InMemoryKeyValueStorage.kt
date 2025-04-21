package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat

/**
 * An in-memory implementation of [KeyValueStorage] and [MutableKeyValueStorage].
 *
 * This class provides a simple, in-memory key-value storage solution suitable for testing or applications where data
 * persistence across sessions is not required. It stores data as strings internally and uses a provided [StringFormat]
 * for serialization and deserialization of values.
 *
 * This implementation is thread-safe, allowing concurrent access and modifications from multiple coroutines. It also
 * supports observing changes to specific keys via Kotlin Flows.
 *
 * @property [format] The [StringFormat] used for serializing and deserializing values to and from strings. Typically,
 * this will be a JSON format, but other string-based formats are supported.
 */
public open class InMemoryKeyValueStorage<Key : Any> public constructor(
    private val format: StringFormat
) : KeyValueStorage<Key>,
    MutableKeyValueStorage<Key> {

    private val storage = mutableMapOf<Key, String>()

    private val listeners = mutableMapOf<Key, MutableStateFlow<String?>>()

    private val mutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    override suspend fun count(): Int = storage.size

    override suspend fun contains(key: Key): Boolean = storage.contains(key)

    override suspend fun <Value : Any> get(key: Key, deserializer: KSerializer<Value>): Value? {
        val stored = storage[key] ?: return null

        return format.decodeFromString(
            deserializer = deserializer,
            string = stored
        )
    }

    override suspend fun <Value : Any> set(key: Key, value: Value?, serializer: KSerializer<Value>) {
        mutex.withLock {
            if (value == null) {
                storage.remove(key)

                emit(key = key, value = null)
            } else {
                val stored = format.encodeToString(
                    serializer = serializer,
                    value = value
                )

                storage[key] = stored

                emit(key = key, value = stored)
            }
        }
    }

    override suspend fun remove(key: Key) {
        mutex.withLock {
            storage.remove(key)

            emit(key = key, value = null)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            val currentKeys = storage.keys

            storage.clear()

            currentKeys.forEach { key ->
                emit(key = key, value = null)
            }
        }
    }

    override fun <Value : Any> flow(key: Key, deserializer: KSerializer<Value>): Flow<Value?> {
        val flow = listeners[key] ?: MutableStateFlow<String?>(null).apply {
            value = storage[key]
        }

        listeners[key] = flow

        return flow.map { stored ->
            if (stored != null) {
                format.decodeFromString(
                    deserializer = deserializer,
                    string = stored
                )
            } else {
                null
            }
        }
    }

    private suspend fun emit(key: Key, value: String?) {
        emitMutex.withLock {
            val listener = listeners[key]

            listener?.value = value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemoryKeyValueStorage<*>) return false

        if (format != other.format) return false
        if (storage != other.storage) return false
        if (listeners != other.listeners) return false
        if (mutex != other.mutex) return false

        return emitMutex == other.emitMutex
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + storage.hashCode()
        result = 31 * result + listeners.hashCode()
        result = 31 * result + mutex.hashCode()
        result = 31 * result + emitMutex.hashCode()
        return result
    }

    override fun toString(): String =
        "InMemoryKeyValueStorage(format=$format, storage=$storage, listeners=$listeners, mutex=$mutex, emitMutex=$emitMutex)"
}
