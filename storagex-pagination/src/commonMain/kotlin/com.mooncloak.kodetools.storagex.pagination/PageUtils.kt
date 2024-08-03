package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Retrieves a [List] of all the items as a result of retrieving all the pages.
 */
public suspend fun <T> paginateAll(
    getPage: suspend (cursor: Cursor?, direction: Direction) -> Page<T>
): List<T> {
    val items = mutableListOf<T>()
    var page: Page<T>? = null

    do {
        page = getPage(
            page?.info?.lastCursor,
            Direction.After
        )

        items.addAll(page.items)
    } while (page != null && page.info.hasNext == true && page.items.isNotEmpty())

    return items
}

/**
 * Retrieves a [Flow] which emits a [List] of all currently loaded the paginated items, which
 * completes when all the pages are loaded.
 */
public suspend fun <T> paginateFlow(
    getPage: suspend (cursor: Cursor?, direction: Direction) -> Page<T>
): Flow<List<T>> = flow {
    val items = mutableListOf<T>()
    var page: Page<T>? = null

    do {
        page = getPage(
            page?.info?.lastCursor,
            Direction.After
        )

        items.addAll(page.items)
        emit(items)
    } while (page != null && page.info.hasNext == true && page.items.isNotEmpty())
}
