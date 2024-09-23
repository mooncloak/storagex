//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[Pager](index.md)

# Pager

[common]\
interface [Pager](index.md)&lt;[Request](index.md), [Filters](index.md), [Result](index.md)&gt;

A stateful abstraction over one or more [PagedDataRepository](../-paged-data-repository/index.md) components.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [loading](loading.md) | [common]<br>abstract val [loading](loading.md): StateFlow&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
| [pages](pages.md) | [common]<br>abstract val [pages](pages.md): StateFlow&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Page](../-page/index.md)&lt;[Result](index.md)&gt;&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [invalidate](invalidate.md) | [common]<br>abstract suspend fun [invalidate](invalidate.md)() |
| [load](load.md) | [common]<br>abstract suspend fun [load](load.md)(request: [Request](index.md), count: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html), cursor: [Cursor](../-cursor/index.md)? = null, direction: [Direction](../-direction/index.md) = Direction.After, sort: [SortOptions](../-sort-options/index.md)? = null, filters: [Filters](index.md)? = null) |
| [load](../load.md) | [common]<br>inline suspend fun &lt;[Request](../load.md), [Filters](../load.md), [Result](../load.md)&gt; [Pager](index.md)&lt;[Request](../load.md), [Filters](../load.md), [Result](../load.md)&gt;.[load](../load.md)(pageRequest: [PageRequest](../-page-request/index.md)&lt;[Request](../load.md), [Filters](../load.md)&gt;) |
| [next](next.md) | [common]<br>abstract suspend fun [next](next.md)() |
| [previous](previous.md) | [common]<br>abstract suspend fun [previous](previous.md)() |
| [refresh](refresh.md) | [common]<br>abstract suspend fun [refresh](refresh.md)() |
