package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.browser.window
import kotlinx.serialization.StringFormat

@Suppress("FunctionName")
public actual fun KeyValueStorage.Companion.Local(format: StringFormat): MutableKeyValueStorage<String> =
    KeyValueStorage.LocalStorage(
        format = format,
        localStorage = window.localStorage
    )
