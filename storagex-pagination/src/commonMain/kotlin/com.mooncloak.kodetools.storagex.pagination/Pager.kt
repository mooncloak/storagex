package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * A stateful abstraction over one or more [PagedDataSource] components that can be used for
 * pagination.
 */
@ExperimentalPaginationAPI
public interface Pager<Item> {

    /**
     * The [Flow] of [PagerStateModel]s that get emitted when the data is updated by invoking the
     * paging functions.
     */
    public val flow: Flow<PagerStateModel<Item>>

    /**
     * Refreshes the data by loading the initial paging data.
     */
    public suspend fun refresh()

    /**
     * Loads at the start of the paging data.
     */
    public suspend fun prepend()

    /**
     * Loads at the end of the paging data.
     */
    public suspend fun append()

    /**
     * A component that can create a [Pager] instance with an initial load request.
     */
    public fun interface Loader<Data : Any, Filters : Any, Item> {

        /**
         * Creates and returns a new [Pager] instance that performs the provided initial [request].
         *
         * @param [request] The initial request that the [Pager] loads.
         *
         * @param [coroutineScope] The [CoroutineScope] that the initial [request] is launched in.
         */
        public fun load(
            request: PageRequest<Data, Filters>,
            coroutineScope: CoroutineScope
        ): Pager<Item>

        public companion object
    }

    public companion object
}

/**
 * Invokes the [Pager.Loader.load] function and waits for the request to finish loading before
 * returning the resulting [Pager] instance.
 */
@ExperimentalPaginationAPI
public suspend fun <Data : Any, Filters : Any, Item> Pager.Loader<Data, Filters, Item>.awaitLoad(
    request: PageRequest<Data, Filters>
): Pager<Item> = coroutineScope {
    this@awaitLoad.load(
        request = request,
        coroutineScope = this
    )
}

/**
 * Creates a [Pager.Loader] instance with the underlying [source].
 */
@ExperimentalPaginationAPI
public fun <Data : Any, Filters : Any, Item> Pager.Loader.Companion.create(
    source: PagedDataSource<Data, Filters, Item>
): Pager.Loader<Data, Filters, Item> = DefaultPagerLoader(
    source = source
)

/**
 * Creates a [Pager.Loader] instance using a [CombinedPagedDataSource] wrapping the provided
 * [sources].
 */
@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any, Item> Pager.Loader.Companion.create(
    vararg sources: PagedDataSource<Data, Filters, Item>,
    format: StringFormat = Json.Default,
    dataSerializer: KSerializer<Data> = format.serializersModule.serializer(),
    filtersSerializer: KSerializer<Filters> = format.serializersModule.serializer()
): Pager.Loader<Data, Filters, Item> = when {
    sources.isEmpty() -> error("At least one `PagedDataSource` MUST be provided to the `Pager.Loader.create` function.")
    sources.size == 1 -> DefaultPagerLoader(source = sources.first())
    else -> DefaultPagerLoader(
        source = CombinedPagedDataSource(
            sources = sources.toList(),
            dataSerializer = dataSerializer,
            filtersSerializer = filtersSerializer,
            format = format
        )
    )
}

/**
 * Creates a [Pager.Loader] instance using a [CombinedPagedDataSource] wrapping the provided
 * [sources].
 */
@ExperimentalPaginationAPI
public inline fun <reified Data : Any, reified Filters : Any, Item> Pager.Loader.Companion.create(
    sources: Collection<PagedDataSource<Data, Filters, Item>>,
    format: StringFormat = Json.Default,
    dataSerializer: KSerializer<Data> = format.serializersModule.serializer(),
    filtersSerializer: KSerializer<Filters> = format.serializersModule.serializer()
): Pager.Loader<Data, Filters, Item> = when {
    sources.isEmpty() -> error("At least one `PagedDataSource` MUST be provided to the `Pager.Loader.create` function.")
    sources.size == 1 -> DefaultPagerLoader(source = sources.first())
    else -> DefaultPagerLoader(
        source = CombinedPagedDataSource(
            sources = sources.toList(),
            dataSerializer = dataSerializer,
            filtersSerializer = filtersSerializer,
            format = format
        )
    )
}
