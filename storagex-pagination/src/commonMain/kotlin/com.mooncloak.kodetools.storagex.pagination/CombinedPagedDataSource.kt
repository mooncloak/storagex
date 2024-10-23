package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
                request.cursor?.

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
                        request = request,
                        pages = pages
                    )
                )
            } catch (e: Throwable) {
                return@supervisorScope PageLoadResult.error(cause = e)
            }
        }
}

@ExperimentalPaginationAPI
internal fun <Data : Any, Filters : Any, Item> PageCursor.Companion.encodeCombined(
    request: PageRequest<Data, Filters>,
    pages: List<ResolvedPage<Item>>
): PageCursor {
    val decoded = CombinedDecodedPageCursor(
        request = request,
        references = pages.map { page ->
            PageReference(
                id = page.id,
                info = page.info
            )
        }
    )

    return PageCursor.encode(
        value = decoded,
        format = Json.Default
    )
}

@ExperimentalPaginationAPI
internal inline fun <reified Data : Any, reified Filters : Any> PageCursor.decodeCombined(): CombinedDecodedPageCursor<Data, Filters> =
    this.decode(format = Json.Default)

@ExperimentalPaginationAPI
@Serializable
internal data class CombinedDecodedPageCursor<Data : Any, Filters : Any> internal constructor(
    @SerialName(value = "request") internal val request: PageRequest<Data, Filters>,
    @SerialName(value = "references") internal val references: List<PageReference> = emptyList()
)

@ExperimentalPaginationAPI
@Serializable
internal data class PageReference internal constructor(
    @SerialName(value = "id") internal val id: String,
    @SerialName(value = "info") internal val info: PageInfo
)
