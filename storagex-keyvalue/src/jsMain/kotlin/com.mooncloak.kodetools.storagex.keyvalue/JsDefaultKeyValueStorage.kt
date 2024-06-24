package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.browser.window
import kotlinx.serialization.StringFormat

public actual operator fun KeyValueStorage.Companion.invoke(format: StringFormat): KeyValueStorage =
    KeyValueStorage.LocalStorage(
        format = format,
        localStorage = window.localStorage
    )
