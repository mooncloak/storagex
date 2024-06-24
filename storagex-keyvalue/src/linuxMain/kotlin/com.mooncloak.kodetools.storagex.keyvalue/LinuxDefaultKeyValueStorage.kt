package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.StringFormat

// FIXME: Linux is using in-memory as default
public actual operator fun KeyValueStorage.Companion.invoke(format: StringFormat): KeyValueStorage =
    InMemory(format = format)
