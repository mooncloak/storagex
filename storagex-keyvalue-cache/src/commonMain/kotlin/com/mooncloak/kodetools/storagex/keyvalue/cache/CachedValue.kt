package com.mooncloak.kodetools.storagex.keyvalue.cache

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents the default internal representation of data stored within a [Cache].
 *
 * @property [timestamp] The [Instant] when the data was cached.
 *
 * @property [expiresIn] The expiration [Duration] of the data.
 *
 * @property [value] The actual data.
 */
@Serializable
public data class CachedValue<T> public constructor(
    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "expires_in") public val expiresIn: Duration,
    @SerialName(value = "value") public val value: T
)

/**
 * Determines whether this [CachedValue] instance is still considered valid at the provided time
 * [instant].
 *
 * @return `true` if this [CachedValue] is still considered valid at the provided time [instant],
 * and therefore the data can still be used, otherwise `false`.
 */
public inline fun <T> CachedValue<T>.isValidAt(instant: Instant): Boolean =
    this.timestamp + this.expiresIn > instant
