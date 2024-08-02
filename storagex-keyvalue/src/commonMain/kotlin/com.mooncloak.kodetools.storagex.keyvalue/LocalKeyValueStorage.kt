package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.StringFormat

/**
 * Creates a new [KeyValueStorage] instance that utilizes the provided [format] for serialization
 * and deserialization of the stored values.
 */
@Suppress("FunctionName")
public expect fun KeyValueStorage.Companion.Local(format: StringFormat): MutableKeyValueStorage<String>
