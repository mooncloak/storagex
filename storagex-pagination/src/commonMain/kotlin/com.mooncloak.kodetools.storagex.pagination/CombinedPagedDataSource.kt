package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@ExperimentalPaginationAPI
public class CombinedPagedDataSource<Data : Any, Filters : Any, Item> public constructor(
    private val sources: List<PagedDataSource<Data, Filters, Item>>,
    override val sourceId: String = "Combined:" + sources.joinToString(separator = ":") { it.sourceId },
    private val format: StringFormat = Json.Default
) : PagedDataSource<Data, Filters, Item> {

    private val sourceById: Map<String, PagedDataSource<Data, Filters, Item>> =
        sources.associateBy { it.sourceId }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun load(request: PageRequest<Data, Filters>): PageLoadResult<Item> =
        supervisorScope {
            try {
                val pageCursor = runCatching {
                    request.cursor?.decodeOrNull<CombinedDecodedPageCursor>(
                        format = format
                    )
                }.getOrNull()

                // I find it more clear to have this as an if-else statement than an elvis operator in this case.
                @Suppress("IfThenToElvis")
                val results = if (pageCursor != null) {
                    // If the PageCursor is available, then it means this is a subsequent request.
                    // So, we need to update the request for each stored reference to load before
                    // or after the first or last cursor for that reference.
                    pageCursor.references.map { reference ->
                        val source = reference.dataSourceId?.let { sourceById[it] }

                        // The direction is for the PageCursor which operates on the entire
                        // PageCollection. So, if we have a direction of "After", then we should
                        // load the page after the last cursor, otherwise, we should load the page
                        // before the first cursor.
                        val referenceCursor = if (request.direction == Direction.After) {
                            reference.info.lastCursor
                        } else {
                            reference.info.firstCursor
                        }

                        // We are retaining the information in the original request, but we are
                        // updating the Cursor to be this reference's Cursor. This way it can load
                        // the previous or next page appropriately.
                        val updatedRequest = request.copy(cursor = referenceCursor)

                        loadFromSource(
                            source = source,
                            request = updatedRequest,
                            reference = null
                        )
                    }.awaitAll()
                        .filterNotNull()
                } else {
                    sources.map { source ->
                        loadFromSource(
                            source = source,
                            request = request,
                            reference = null
                        )
                    }.awaitAll()
                        .filterNotNull()
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
                        pages = pages,
                        format = format
                    )
                )
            } catch (e: Throwable) {
                return@supervisorScope PageLoadResult.error(cause = e)
            }
        }

    private suspend fun loadFromSource(
        source: PagedDataSource<Data, Filters, Item>?,
        request: PageRequest<Data, Filters>,
        reference: PageReference?
    ): Deferred<PageLoadResult<Item>?> = coroutineScope {
        async {
            when {
                source == null -> null
                reference == null -> source.load(request = request)
                request.direction == Direction.Before && (reference.info.hasPrevious == false || reference.info.firstCursor == null) -> null
                request.direction == Direction.After && (reference.info.hasNext == false || reference.info.lastCursor == null) -> null
                else -> source.load(request = request)
            }
        }
    }
}
