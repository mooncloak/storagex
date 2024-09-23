//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PagedDataRepository](index.md)/[load](load.md)

# load

[common]\
abstract suspend fun [load](load.md)(request: [Request](index.md), count: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html), cursor: [Cursor](../-cursor/index.md)? = null, direction: [Direction](../-direction/index.md) = Direction.After, sort: [SortOptions](../-sort-options/index.md)? = null, filters: [Filters](index.md)? = null): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](index.md)&gt;

abstract suspend fun [load](load.md)(cursor: [Cursor](../-cursor/index.md), direction: [Direction](../-direction/index.md)): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](index.md)&gt;
