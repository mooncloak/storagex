package com.mooncloak.kodetools.storagex.pagination

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@ExperimentalPaginationAPI
internal class MultipleDataSourcePager<Request, Filters, Result> internal constructor(
    private val sources: List<PagedDataRepository<Request, Filters, Result>>
) : Pager<Request, Filters, Result> {

    override val loading: StateFlow<Boolean>
        get() = mutableLoading

    override val pages: StateFlow<List<Page<Result>>>
        get() = mutablePages

    private val mutablePages = MutableStateFlow<List<Page<Result>>>(emptyList())
    private val mutableLoading = MutableStateFlow(false)

    private val sourcesById: Map<String, PagedDataRepository<Request, Filters, Result>> =
        sources.associateBy { it.sourceId }

    private val mutex = Mutex(locked = false)

    override suspend fun invalidate() {
        mutex.withLock {
            mutablePages.value = emptyList()
            mutableLoading.value = false
        }
    }

    override suspend fun refresh() {
        mutex.withLock {
            try {
                mutableLoading.value = true

                var currentPages = pages.value

                if (currentPages.isNotEmpty()) {
                    val pageMutex = Mutex(locked = false)

                    val currentPage = currentPages.last()
                    currentPages = currentPages.dropLast(1)

                    supervisorScope {
                        when (currentPage) {
                            is PageCollection<Result> -> {
                                val collectionPages = mutableListOf<ResolvedPage<Result>>()

                                currentPage.pages.map { page ->
                                    async {
                                        refreshCurrentPage(currentPage = page)?.let { refreshedPage ->
                                            pageMutex.withLock {
                                                collectionPages.add(refreshedPage)

                                                mutablePages.value =
                                                    currentPages + PageCollection(pages = collectionPages)
                                            }
                                        }
                                    }
                                }.awaitAll()
                            }

                            is ResolvedPage<Result> -> {
                                async {
                                    refreshCurrentPage(currentPage = currentPage)?.let { refreshedPage ->
                                        mutablePages.value =
                                            currentPages + PageCollection(pages = listOf(refreshedPage))
                                    }
                                }
                            }

                            is PagePlaceholder<*> -> error("PagePlaceholders are not currently supported in ${this@MultipleDataSourcePager::class.simpleName}.")
                        }
                    }
                }
            } finally {
                mutableLoading.value = false
            }
        }
    }

    override suspend fun previous() {
        mutex.withLock {
            try {
                mutableLoading.value = true

                val currentPages = pages.value

                if (currentPages.isEmpty()) {
                    error("Cannot load previous page as there is no current page loaded. First load a page before invoking previous.")
                }

                if (currentPages.size > 1) {
                    // We already loaded the previous page, so drop the current to display the last page as the most recent.
                    mutablePages.value = currentPages.dropLast(1)
                } else {
                    // We need to load the previous page.
                    val pageMutex = Mutex(locked = false)

                    supervisorScope {
                        when (val currentPage = currentPages.last()) {
                            is PageCollection<Result> -> {
                                val collectionPages = mutableListOf<ResolvedPage<Result>>()

                                currentPage.pages.map { page ->
                                    async {
                                        loadPreviousPage(currentPage = page)?.let { previousPage ->
                                            pageMutex.withLock {
                                                collectionPages.add(previousPage)

                                                mutablePages.value =
                                                    listOf(PageCollection(pages = collectionPages)) + currentPages
                                            }
                                        }
                                    }
                                }.awaitAll()
                            }

                            is ResolvedPage<Result> -> {
                                async {
                                    loadPreviousPage(currentPage = currentPage)?.let { previousPage ->
                                        mutablePages.value = currentPages + PageCollection(pages = listOf(previousPage))
                                    }
                                }
                            }

                            is PagePlaceholder<*> -> error("PagePlaceholders are not currently supported in ${this@MultipleDataSourcePager::class.simpleName}.")
                        }
                    }
                }
            } finally {
                mutableLoading.value = false
            }
        }
    }

    override suspend fun next() {
        mutex.withLock {
            try {
                mutableLoading.value = true

                val pageMutex = Mutex(locked = false)

                val currentPages = pages.value

                if (currentPages.isEmpty()) {
                    error("Cannot load next page as there is no current page loaded. First load a page before invoking next.")
                }

                supervisorScope {
                    when (val currentPage = currentPages.last()) {
                        is PageCollection<Result> -> {
                            val collectionPages = mutableListOf<ResolvedPage<Result>>()

                            currentPage.pages.map { page ->
                                async {
                                    loadNextPage(currentPage = page)?.let { nextPage ->
                                        pageMutex.withLock {
                                            collectionPages.add(nextPage)

                                            mutablePages.value = currentPages + PageCollection(pages = collectionPages)
                                        }
                                    }
                                }
                            }.awaitAll()
                        }

                        is ResolvedPage<Result> -> {
                            async {
                                loadNextPage(currentPage = currentPage)?.let { nextPage ->
                                    mutablePages.value = currentPages + PageCollection(pages = listOf(nextPage))
                                }
                            }
                        }

                        is PagePlaceholder<*> -> error("PagePlaceholders are not currently supported in ${this@MultipleDataSourcePager::class.simpleName}.")
                    }
                }
            } finally {
                mutableLoading.value = false
            }
        }
    }

    override suspend fun load(
        request: Request,
        count: UInt,
        cursor: Cursor?,
        direction: Direction,
        sort: SortOptions?,
        filters: Filters?
    ) {
        mutex.withLock {
            try {
                mutableLoading.value = true

                val pageMutex = Mutex(locked = false)

                val pages = mutableListOf<ResolvedPage<Result>>()

                supervisorScope {
                    sources.map { source ->
                        async {
                            val page = source.load(
                                request = request,
                                count = count,
                                cursor = cursor,
                                direction = direction,
                                sort = sort,
                                filters = filters
                            )

                            pageMutex.withLock {
                                pages.add(page)

                                mutablePages.value = listOf(PageCollection(pages = pages))
                            }
                        }
                    }.awaitAll()
                }
            } finally {
                mutableLoading.value = false
            }
        }
    }

    private suspend inline fun refreshCurrentPage(
        currentPage: ResolvedPage<Result>
    ): ResolvedPage<Result>? = loadRelativePage(
        currentPage = currentPage,
        cursor = { this.info.firstCursor },
        condition = { true },
        load = { source, cursor -> source.after(cursor = cursor) }
    )

    private suspend inline fun loadPreviousPage(
        currentPage: ResolvedPage<Result>
    ): ResolvedPage<Result>? = loadRelativePage(
        currentPage = currentPage,
        cursor = { this.info.firstCursor },
        condition = { this.info.hasPrevious != false },
        load = { source, cursor -> source.before(cursor = cursor) }
    )

    private suspend inline fun loadNextPage(
        currentPage: ResolvedPage<Result>
    ): ResolvedPage<Result>? = loadRelativePage(
        currentPage = currentPage,
        cursor = { this.info.lastCursor },
        condition = { this.info.hasNext != false },
        load = { source, cursor -> source.after(cursor = cursor) }
    )

    private suspend fun loadRelativePage(
        currentPage: ResolvedPage<Result>,
        cursor: ResolvedPage<Result>.() -> Cursor?,
        condition: ResolvedPage<Result>.() -> Boolean,
        load: suspend (source: PagedDataRepository<Request, Filters, Result>, cursor: Cursor) -> ResolvedPage<Result>
    ): ResolvedPage<Result>? = sourcesById[currentPage.pageSourceId]?.let { source ->
        val loadedCursor = currentPage.cursor()

        return if (currentPage.condition() && loadedCursor != null) {
            load(source, loadedCursor)
        } else {
            null
        }
    }
}
