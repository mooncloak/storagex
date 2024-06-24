package com.mooncloak.kodetools.storagex.keyvalue

import com.russhwolf.settings.Settings
import kotlinx.serialization.StringFormat

public actual operator fun KeyValueStorage.Companion.invoke(format: StringFormat): KeyValueStorage =
    KeyValueStorage.Settings(
        settings = Settings(),
        serializersModule = format.serializersModule
    )
