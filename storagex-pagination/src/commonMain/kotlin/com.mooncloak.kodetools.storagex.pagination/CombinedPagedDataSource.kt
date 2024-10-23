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

    private val sourceById: Map<String, PagedDataSource<Data, Filters, Item>> =
        sources.associateBy { it.sourceId }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> =
        supervisorScope {
            try {
                val pageCursor = runCatching {
                    request.cursor?.decode<CombinedDecodedPageCursor<Data, Filters>>()
                }.getOrNull()

                if (pageCursor != null) {
                    val updatedRequest = pageCursor.request.copy(
                        direction = request.direction
                    )

                    val results = pageCursor.references.mapNotNull { reference ->
                        val source = reference.dataSourceId?.let { sourceById[it] }

                        when {
                            source == null -> null
                            request.direction == Direction.Before && (reference.info.hasPrevious == false || reference.info.firstCursor == null) -> null
                            request.direction == Direction.After && (reference.info.hasNext == false || reference.info.lastCursor == null) -> null
                            else -> source.load(request = updatedRequest)
                        }
                    }

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
                        pageCursor = Cursor.encode(
                            request = request,
                            pages = pages
                        )
                    )
                } else {
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
                        pageCursor = Cursor.encode(
                            request = request,
                            pages = pages
                        )
                    )
                }
            } catch (e: Throwable) {
                return@supervisorScope PageLoadResult.error(cause = e)
            }
        }
}
