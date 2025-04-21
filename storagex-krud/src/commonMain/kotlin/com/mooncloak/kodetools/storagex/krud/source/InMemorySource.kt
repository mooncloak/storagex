package com.mooncloak.kodetools.storagex.krud.source

import com.mooncloak.kodetools.storagex.krud.MutableRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public open class InMemorySource<ID : Any, Value : Any> public constructor(
    initialValues: Map<ID, Value> = emptyMap()
) : MutableRepository<ID, Value> {

    private val storage = mutableMapOf<ID, Value>().apply {
        putAll(initialValues)
    }

    private val mutex = Mutex(locked = false)

    override suspend fun count(): Int =
        storage.size

    override suspend fun contains(id: ID): Boolean =
        storage.contains(id)

    override suspend fun get(id: ID): Value =
        storage[id] ?: throw NoSuchElementException("No item found for the provided id '$id'.")

    override suspend fun get(count: Int, offset: Int): List<Value> =
        storage.values.toList().drop(offset).take(count)

    override suspend fun getAll(): List<Value> =
        storage.values.toList()

    override suspend fun insert(id: ID, value: () -> Value): Value =
        mutex.withLock {
            val item = value.invoke()

            storage[id] = item

            item
        }

    override suspend fun update(id: ID, update: Value.() -> Value): Value =
        mutex.withLock {
            val current = storage[id] ?: throw NoSuchElementException("No item found for the provided id '$id'.")

            val updated = current.update()

            storage[id] = updated

            updated
        }

    override suspend fun remove(id: ID) {
        mutex.withLock {
            storage.remove(id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            storage.clear()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InMemorySource<*, *>) return false

        if (storage != other.storage) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = storage.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String = "InMemorySource(storage=$storage, mutex=$mutex)"
}
