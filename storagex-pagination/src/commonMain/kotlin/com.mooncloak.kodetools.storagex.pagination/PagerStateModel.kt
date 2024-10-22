package com.mooncloak.kodetools.storagex.pagination

@ExperimentalPaginationAPI
public data class PagerStateModel<Item> public constructor(
    public val pages: List<Page<Item>> = emptyList(),
    public val prepend: LoadState = LoadState.Incomplete,
    public val append: LoadState = LoadState.Incomplete,
    public val refresh: LoadState = LoadState.Incomplete
)
