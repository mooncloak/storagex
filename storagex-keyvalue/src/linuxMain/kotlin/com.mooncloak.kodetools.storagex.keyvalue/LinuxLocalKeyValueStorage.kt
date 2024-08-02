package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.StringFormat

// FIXME: Linux is using in-memory as default
@Suppress("FunctionName")
public actual fun KeyValueStorage.Companion.Local(format: StringFormat): MutableKeyValueStorage<String> =
    InMemory()
