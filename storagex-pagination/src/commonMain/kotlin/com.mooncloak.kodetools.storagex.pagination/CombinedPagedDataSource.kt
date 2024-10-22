package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExperimentalPaginationAPI
public class CombinedPagedDataSource<Data : Any, Filters : Any, Item> public constructor(
    private val sources: List<PagedDataSource<Data, Filters, Item>>,
    override val sourceId: String = "Combined:" + sources.joinToString(separator = ":") { it.sourceId }
) : PagedDataSource<Data, Filters, Item> {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> =
        supervisorScope {
            try {
                val results = sources.map { source ->
                    async {
                        source.load(request = request)
                    }
                }.awaitAll()

                val pages = results.map { result ->
                    when (result) {
                        is InvalidResult -> return@supervisorScope result
                        is ErrorResult<Item> -> return@supervisorScope result
                        is Page<Item> -> result.toResolvedPage()
                    }
                }

                return@supervisorScope PageLoadResult.pageCollection(
                    id = Uuid.random().toHexString(),
                    dataSourceId = sourceId,
                    pages = pages,
                    pageCursor = PageCursor.encodeCombined(
                        pages = pages
                    )
                )
            } catch (e: Throwable) {
                return@supervisorScope PageLoadResult.error(cause = e)
            }
        }
}
