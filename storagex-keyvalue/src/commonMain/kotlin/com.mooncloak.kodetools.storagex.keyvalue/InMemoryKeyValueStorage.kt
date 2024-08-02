package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy

/**
 * Creates an instance of the [KeyValueStorage] interface where the underlying values are stored in
 * an in-memory [Map].
 *
 * @return [KeyValueStorage]
 */
@Suppress("FunctionName")
public fun <Key : Any> KeyValueStorage.Companion.InMemory(): MutableKeyValueStorage<Key> =
    InMemoryKeyValueStorage(format = EmptySerialFormat)

internal class InMemoryKeyValueStorage<Key : Any> internal constructor(
    override val format: SerialFormat
) : MutableKeyValueStorage<Key> {

    private val map = mutableMapOf<Key, KeyValueStorage.StoredValue<*>>()

    private val mutex = Mutex(locked = false)

    private val changes = MutableStateFlow<Pair<Key, KeyValueStorage.StoredValue<*>?>?>(null)

    override suspend fun size(): Long = map.size.toLong()

    override suspend fun entries(): Set<KeyValueStorage.Entry<Key, *>> =
        map.entries.map {
            KeyValueStorage.Entry(
                key = it.key,
                storedValue = it.value
            )
        }.toSet()

    override suspend fun getValue(key: Key): KeyValueStorage.StoredValue<*> =
        map[key] ?: throw NoSuchElementException("No entry found with key '$key'.")

    override suspend fun containsKey(key: Key): Boolean = map.contains(key)

    override suspend fun <Value : Any> put(
        key: Key,
        serializer: SerializationStrategy<Value>,
        value: Value?
    ): KeyValueStorage.StoredValue<*>? {
        mutex.withLock {
            if (value == null) {
                val previousValue = map.remove(key)

                changes.value = key to null

                return previousValue
            } else {
                val previousValue = map[key]

                val storedValue = KeyValueStorage.StoredValue(
                    rawValue = value,
                    format = format
                )

                map[key] = storedValue

                changes.value = key to storedValue

                return previousValue
            }
        }
    }

    override suspend fun <Value> putAll(entries: Collection<MutableKeyValueStorage.InputEntry<Key, Value>>) {
        mutex.withLock {
            entries.forEach { entry ->
                val storedValue = KeyValueStorage.StoredValue(
                    rawValue = entry.inputValue.value,
                    format = format
                )

                map[entry.key] = storedValue

                changes.value = entry.key to storedValue
            }
        }
    }

    override suspend fun remove(key: Key): KeyValueStorage.StoredValue<*>? =
        mutex.withLock {
            val removedValue = map.remove(key = key)

            changes.value = key to null

            removedValue
        }

    override suspend fun clear() {
        mutex.withLock {
            if (map.isEmpty()) return

            val existingItems = map.toMap()

            map.clear()

            existingItems.forEach { entry ->
                changes.emit(entry.key to entry.value)
            }

            changes.value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemoryKeyValueStorage<*>) return false

        if (format != other.format) return false
        if (map != other.map) return false
        if (mutex != other.mutex) return false

        return changes == other.changes
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + map.hashCode()
        result = 31 * result + mutex.hashCode()
        result = 31 * result + changes.hashCode()
        return result
    }

    override fun toString(): String =
        "InMemoryKeyValueStorage(format=$format, map=$map, mutex=$mutex, changes=$changes)"
}
