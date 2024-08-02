package com.mooncloak.kodetools.storagex.keyvalue

import kotlinx.serialization.SerialFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

internal data object EmptySerialFormat : SerialFormat {

    override val serializersModule: SerializersModule
        get() = EmptySerializersModule()
}
