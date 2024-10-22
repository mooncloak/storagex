package com.mooncloak.kodetools.storagex.pagination

import androidx.paging.PagingSource

@ExperimentalPaginationAPI
public suspend fun <Item : Any> PageLoadResult<Item>.toPagingLoadResult(): PagingSource.LoadResult<Cursor, Item> =
    when (this) {
        is InvalidResult -> PagingSource.LoadResult.Invalid()

        is ErrorResult<Item> -> PagingSource.LoadResult.Error(throwable = this.cause)

        is Page<Item> -> {
            val info = (this as? ResolvedPage<Item>)?.info

            PagingSource.LoadResult.Page(
                data = this.get(),
                prevKey = info?.firstCursor,
                nextKey = info?.lastCursor
            )
        }
    }
