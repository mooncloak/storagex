package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@ExperimentalPaginationAPI
internal class DefaultPager<Data : Any, Filters : Any, Item> internal constructor(
    initialCoroutineScope: CoroutineScope,
    private val initialRequest: PageRequest<Data, Filters>,
    private val source: PagedDataSource<Data, Filters, Item>,
) : Pager<Item> {

    override val flow: Flow<PagerStateModel<Item>>
        get() = mutableFlow

    private val mutableFlow = MutableStateFlow<PagerStateModel<Item>>(PagerStateModel())

    private val mutex = Mutex(locked = false)

    init {
        initialCoroutineScope.launch {
            mutex.withLock {
                mutableFlow.value = mutableFlow.value.copy(refresh = LoadState.Loading)

                val updatedValue = performLoad(
                    state = mutableFlow.value,
                    request = initialRequest,
                    type = LoadType.Refresh
                )

                mutableFlow.value = updatedValue
            }
        }
    }

    override suspend fun refresh() {
        mutex.withLock {
            // Refresh should never reach end of pagination. So, we don't perform that check here.

            mutableFlow.value = mutableFlow.value.copy(refresh = LoadState.Loading)

            val updatedValue = performLoad(
                state = mutableFlow.value,
                request = initialRequest,
                type = LoadType.Refresh
            )

            mutableFlow.value = updatedValue
        }
    }

    override suspend fun prepend() {
        mutex.withLock {
            if (mutableFlow.value.prepend.endOfPaginationReached) {
                return
            }

            val currentState = mutableFlow.value.copy(prepend = LoadState.Loading)

            mutableFlow.value = currentState

            val firstPage = currentState.pages.firstOrNull()

            val request = firstPage?.toResolvedPage()
                ?.info
                ?.firstCursor
                ?.let { cursor ->
                    PageRequest<Data, Filters>(
                        cursor = cursor,
                        direction = Direction.Before
                    )
                }

            val updatedState = when {
                firstPage == null -> currentState.copy(
                    prepend = LoadState.Error(
                        cause = IllegalStateException("Cannot prepend when the initial request was not loaded.")
                    )
                )

                request == null -> currentState.copy(
                    prepend = LoadState.Error(
                        cause = IllegalStateException("Cannot append when there is no cursor.")
                    )
                )

                else -> performLoad(
                    state = currentState,
                    request = request,
                    type = LoadType.Prepend
                )
            }

            mutableFlow.value = updatedState
        }
    }

    override suspend fun append() {
        mutex.withLock {
            if (mutableFlow.value.append.endOfPaginationReached) {
                return
            }

            val currentState = mutableFlow.value.copy(append = LoadState.Loading)

            mutableFlow.value = currentState

            val lastPage = currentState.pages.lastOrNull()

            val request = lastPage?.toResolvedPage()
                ?.info
                ?.firstCursor
                ?.let { cursor ->
                    PageRequest<Data, Filters>(
                        cursor = cursor,
                        direction = Direction.After
                    )
                }

            val updatedState = when {
                lastPage == null -> currentState.copy(
                    append = LoadState.Error(
                        cause = IllegalStateException("Cannot append when the initial request was not loaded.")
                    )
                )

                request == null -> currentState.copy(
                    append = LoadState.Error(
                        cause = IllegalStateException("Cannot append when there is no cursor.")
                    )
                )

                else -> performLoad(
                    state = currentState,
                    request = request,
                    type = LoadType.Append
                )
            }

            mutableFlow.value = updatedState
        }
    }

    private suspend fun performLoad(
        state: PagerStateModel<Item>,
        request: PageRequest<Data, Filters>,
        type: LoadType
    ): PagerStateModel<Item> {
        val result = source.load(request = request)

        val loadState = when (result) {
            is InvalidResult -> LoadState.Incomplete // TODO: Do we need the invalid state?

            is ErrorResult<Item> -> LoadState.Error(cause = result.cause)

            is Page<Item> -> LoadState.Complete
        }

        val page = when (result) {
            is Page<Item> -> result
            else -> null
        }

        return when (type) {
            LoadType.Append -> state.copy(
                append = loadState,
                pages = if (page == null) {
                    state.pages
                } else {
                    state.pages + page
                }
            )

            LoadType.Prepend -> state.copy(
                prepend = loadState,
                pages = if (page == null) {
                    state.pages
                } else {
                    listOf(page) + state.pages
                }
            )

            LoadType.Refresh -> state.copy(
                refresh = loadState,
                pages = page?.let { listOf(it) } ?: emptyList()
            )
        }
    }
}

@ExperimentalPaginationAPI
internal class DefaultPagerLoader<Data : Any, Filters : Any, Item> internal constructor(
    private val source: PagedDataSource<Data, Filters, Item>
) : Pager.Loader<Data, Filters, Item> {

    override fun load(
        request: PageRequest<Data, Filters>,
        coroutineScope: CoroutineScope
    ): Pager<Item> = DefaultPager(
        initialCoroutineScope = coroutineScope,
        initialRequest = request,
        source = source
    )
}
