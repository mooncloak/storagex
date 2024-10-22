package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@ExperimentalPaginationAPI
@Serializable
@JvmInline
public value class SortOrder public constructor(
    public val value: String
) {

    public companion object {

        public val Ascending: SortOrder = SortOrder(value = "asc")
        public val Descending: SortOrder = SortOrder(value = "desc")
        public val Furthest: SortOrder = SortOrder(value = "furthest")
        public val Closest: SortOrder = SortOrder(value = "closest")
    }
}
