package com.mooncloak.kodetools.storagex.keyvalue

import com.russhwolf.settings.Settings
import kotlinx.serialization.StringFormat

@Suppress("FunctionName")
public actual fun KeyValueStorage.Companion.Local(format: StringFormat): MutableKeyValueStorage<String> =
    KeyValueStorage.Settings(
        settings = Settings(),
        format = format
    )
