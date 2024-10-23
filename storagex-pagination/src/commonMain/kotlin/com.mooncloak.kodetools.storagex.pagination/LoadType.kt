package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@ExperimentalPaginationAPI
@Serializable
public enum class LoadType(
    public val serialName: String
) {

    @SerialName(value = "refresh")
    Refresh(serialName = "refresh"),

    @SerialName(value = "prepend")
    Prepend(serialName = "prepend"),

    @SerialName(value = "append")
    Append(serialName = "append");

    public companion object {

        public operator fun get(value: String): LoadType? =
            LoadType.entries.firstOrNull { it.serialName.equals(value, ignoreCase = true) }
    }
}
