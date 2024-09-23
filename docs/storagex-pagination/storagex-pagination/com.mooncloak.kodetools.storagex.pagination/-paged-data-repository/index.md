//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PagedDataRepository](index.md)

# PagedDataRepository

[common]\
interface [PagedDataRepository](index.md)&lt;[Request](index.md), [Filters](index.md), [Result](index.md)&gt;

A stateless repository abstraction over a data source whose underlying data can be paginated through.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [sourceId](source-id.md) | [common]<br>abstract val [sourceId](source-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Functions

| Name | Summary |
|---|---|
| [after](../after.md) | [common]<br>inline suspend fun &lt;[Result](../after.md)&gt; [PagedDataRepository](index.md)&lt;*, *, [Result](../after.md)&gt;.[after](../after.md)(cursor: [Cursor](../-cursor/index.md)): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](../after.md)&gt; |
| [before](../before.md) | [common]<br>inline suspend fun &lt;[Result](../before.md)&gt; [PagedDataRepository](index.md)&lt;*, *, [Result](../before.md)&gt;.[before](../before.md)(cursor: [Cursor](../-cursor/index.md)): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](../before.md)&gt; |
| [load](load.md) | [common]<br>abstract suspend fun [load](load.md)(cursor: [Cursor](../-cursor/index.md), direction: [Direction](../-direction/index.md)): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](index.md)&gt;<br>abstract suspend fun [load](load.md)(request: [Request](index.md), count: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html), cursor: [Cursor](../-cursor/index.md)? = null, direction: [Direction](../-direction/index.md) = Direction.After, sort: [SortOptions](../-sort-options/index.md)? = null, filters: [Filters](index.md)? = null): [ResolvedPage](../-resolved-page/index.md)&lt;[Result](index.md)&gt; |
