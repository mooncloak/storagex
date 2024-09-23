//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PageRequest](index.md)

# PageRequest

[common]\
@Serializable

@SerialName(value = &quot;filter&quot;)

data class [PageRequest](index.md)&lt;[Request](index.md), [Filter](index.md)&gt;

Represents a request to a paginated data source.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [count](count.md) | [common]<br>@SerialName(value = &quot;count&quot;)<br>val [count](count.md): [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)<br>The amount of items requested to be loaded. |
| [cursor](cursor.md) | [common]<br>@SerialName(value = &quot;cursor&quot;)<br>val [cursor](cursor.md): [Cursor](../-cursor/index.md)? = null<br>The [Cursor](../-cursor/index.md) provided to the request. |
| [direction](direction.md) | [common]<br>@SerialName(value = &quot;direction&quot;)<br>val [direction](direction.md): [Direction](../-direction/index.md)<br>The [Direction](../-direction/index.md) requested relative to the [cursor](cursor.md). |
| [filters](filters.md) | [common]<br>@SerialName(value = &quot;filters&quot;)<br>val [filters](filters.md): [Filter](index.md)? = null<br>A model that filters the results of the pagination request. |
| [request](request.md) | [common]<br>@SerialName(value = &quot;request&quot;)<br>val [request](request.md): [Request](index.md)<br>The query [Request](index.md) value provided to the page request. This may not be relevant to the request and therefore may be `null`. |
| [sort](sort.md) | [common]<br>@SerialName(value = &quot;sort&quot;)<br>val [sort](sort.md): [SortOptions](../-sort-options/index.md)? = null<br>The [SortOptions](../-sort-options/index.md) defining the order of the results of the pagination request. |
