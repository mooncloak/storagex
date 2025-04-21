package com.mooncloak.kodetools.storagex.keyvalue.cache

import kotlinx.datetime.Clock
import kotlinx.serialization.StringFormat
import kotlin.time.Duration

public actual fun Cache.Companion.create(
    format: StringFormat,
    maxSize: Int?,
    expirationAfterWrite: Duration?,
    clock: Clock
): Cache<String> = InMemoryCache( // TODO: Support maxSize
    format = format,
    clock = clock,
    expirationAfterWrite = expirationAfterWrite
)
