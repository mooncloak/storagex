package com.mooncloak.kodetools.storagex.keyvalue

import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat

@Suppress("FunctionName")
public fun KeyValueStorage.Companion.Settings(
    format: StringFormat,
    settings: Settings,
    keyPrefix: String? = null
): MutableKeyValueStorage<String> = SettingsKeyValueStorage(
    format = format,
    settings = settings,
    keyPrefix = keyPrefix
)

internal class SettingsKeyValueStorage internal constructor(
    override val format: StringFormat,
    private val settings: Settings,
    private val keyPrefix: String? = null
) : MutableKeyValueStorage<String> {

    private val mutex = Mutex(locked = false)

    private val changes = MutableStateFlow<Pair<String, KeyValueStorage.StoredValue<*>?>?>(null)

    override suspend fun size(): Long = settings.size.toLong()

    override suspend fun entries(): Set<KeyValueStorage.Entry<String, *>> =
        mutex.withLock {
            return settings.keys.mapNotNull { key ->
                val rawValue: String? = settings[key]
                val storedValue = rawValue?.let {
                    KeyValueStorage.StoredValue(
                        rawValue = it,
                        format = format
                    )
                }

                storedValue?.let {
                    KeyValueStorage.Entry(
                        key = key,
                        storedValue = it
                    )
                }
            }.toSet()
        }

    override suspend fun getValue(key: String): KeyValueStorage.StoredValue<*> {
        val rawValue: String = settings[key.asFormattedKey()]
            ?: throw NoSuchElementException("No entry found with key '$key'.")

        return KeyValueStorage.StoredValue(
            rawValue = rawValue,
            format = format
        )
    }

    override suspend fun containsKey(key: String): Boolean =
        settings.contains(key.asFormattedKey())

    override fun changes(key: String): Flow<KeyValueStorage.StoredValue<*>?> =
        changes.filter { pair -> key.asFormattedKey() == pair?.first }
            .map { pair -> pair?.second }

    override suspend fun <Value : Any> put(
        key: String,
        serializer: SerializationStrategy<Value>,
        value: Value?
    ): KeyValueStorage.StoredValue<*>? =
        mutex.withLock {
            val formattedKey = key.asFormattedKey()
            val previousRawValue: String? = settings[formattedKey]
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            if (value == null) {
                settings.remove(formattedKey)

                emitChange(key = formattedKey, value = null)
            } else {
                val rawValue = format.encodeToString(
                    serializer = serializer,
                    value = value
                )
                val storedValue = KeyValueStorage.StoredValue(
                    rawValue = rawValue,
                    format = format
                )

                settings[formattedKey] = rawValue

                emitChange(key = formattedKey, value = storedValue)
            }

            return previousStoredValue
        }

    override suspend fun <Value> putAll(entries: Collection<MutableKeyValueStorage.InputEntry<String, Value>>) {
        mutex.withLock {
            entries.forEach { entry ->
                val formattedKey = entry.key.asFormattedKey()
                val value = entry.inputValue.value

                if (value == null) {
                    settings.remove(formattedKey)

                    emitChange(key = formattedKey, value = null)
                } else {
                    val rawValue = format.encodeToString(
                        serializer = entry.inputValue.serializer,
                        value = value
                    )
                    val storedValue = KeyValueStorage.StoredValue(
                        rawValue = rawValue,
                        format = format
                    )

                    settings[formattedKey] = rawValue

                    emitChange(key = formattedKey, value = storedValue)
                }
            }
        }
    }

    override suspend fun remove(key: String): KeyValueStorage.StoredValue<*>? =
        mutex.withLock {
            val formattedKey = key.asFormattedKey()
            val previousRawValue: String? = settings[formattedKey]
            val previousStoredValue = previousRawValue?.let {
                KeyValueStorage.StoredValue(
                    rawValue = it,
                    format = format
                )
            }

            settings.remove(formattedKey)

            emitChange(key = formattedKey, value = null)

            return previousStoredValue
        }

    override suspend fun clear() {
        mutex.withLock {
            val entries = settings.keys.map { key ->
                val rawValue: String? = settings[key]
                val storedValue = rawValue?.let {
                    KeyValueStorage.StoredValue(
                        rawValue = it,
                        format = format
                    )
                }

                key to storedValue
            }

            settings.clear()

            entries.forEach { pair ->
                emitChange(
                    key = pair.first,
                    value = pair.second
                )
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SettingsKeyValueStorage) return false

        if (format != other.format) return false
        if (settings != other.settings) return false
        if (keyPrefix != other.keyPrefix) return false
        if (mutex != other.mutex) return false

        return changes == other.changes
    }

    override fun hashCode(): Int {
        var result = format.hashCode()
        result = 31 * result + settings.hashCode()
        result = 31 * result + (keyPrefix?.hashCode() ?: 0)
        result = 31 * result + mutex.hashCode()
        result = 31 * result + changes.hashCode()
        return result
    }

    override fun toString(): String =
        "SettingsKeyValueStorage(format=$format, settings=$settings, keyPrefix=$keyPrefix, mutex=$mutex, changes=$changes)"

    private suspend fun emitChange(
        key: String,
        value: KeyValueStorage.StoredValue<*>?
    ) {
        changes.emit(key to value)
    }

    private fun String.asFormattedKey(): String =
        if (keyPrefix != null) {
            "$keyPrefix$this"
        } else {
            this
        }
}
