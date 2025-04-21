package com.mooncloak.kodetools.storagex.keyvalue.cache

import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import kotlinx.datetime.Clock
import kotlinx.serialization.StringFormat
import kotlin.time.Duration

/**
 * A [MutableKeyValueStorage] implementation specifically used as a cache.
 */
public interface Cache<Key : Any> : MutableKeyValueStorage<Key> {

    public companion object
}

/**
 * Retrieves a new [Cache] instance using the provided values.
 *
 * @param [format] The [StringFormat] used to serializer and deserialize the cache values to and from their stored
 * [String] values.
 *
 * @param [maxSize] The maximum amount of items to store in the cache. If `null` is provided, no maximum will be set.
 * Defaults to `null`.
 *
 * @param [expirationAfterWrite] The [Duration] representing the time period to expire an item after it has been
 * written to the cache. If `null` is provided, no expiration period will be set.
 *
 * @param [clock] The [Clock] value that may be used by the implementation for expiration time comparisons.
 */
public expect fun Cache.Companion.create(
    format: StringFormat,
    maxSize: Int? = null,
    expirationAfterWrite: Duration? = null,
    clock: Clock = Clock.System
): Cache<String>
