package com.mooncloak.kodetools.storagex.pagination

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Serves a similar purpose to a [Cursor] but for [Page]s instead of items in a [Page]. This is a distinct type for
 * explicit distinction between the two use-cases.
 */
@ExperimentalPaginationAPI
@JvmInline
@Serializable
public value class PageCursor public constructor(
    public val value: String
)
