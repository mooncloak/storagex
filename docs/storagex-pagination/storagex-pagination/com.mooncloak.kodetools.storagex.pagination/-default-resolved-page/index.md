//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[DefaultResolvedPage](index.md)

# DefaultResolvedPage

[common]\
@Serializable

@SerialName(value = &quot;resolved&quot;)

data class [DefaultResolvedPage](index.md)&lt;[Request](index.md), [Filter](index.md), [Type](index.md)&gt;(val pageSourceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt; = emptyList(), val info: [PageInfo](../-page-info/index.md) = PageInfo(), val original: [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;? = null, val actual: [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;? = original) : [ResolvedPage](../-resolved-page/index.md)&lt;[Type](index.md)&gt; 

Represents a page of information loaded from a paginated data source.

## Constructors

| | |
|---|---|
| [DefaultResolvedPage](-default-resolved-page.md) | [common]<br>constructor(pageSourceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt; = emptyList(), info: [PageInfo](../-page-info/index.md) = PageInfo(), original: [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;? = null, actual: [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;? = original) |

## Properties

| Name | Summary |
|---|---|
| [actual](actual.md) | [common]<br>@SerialName(value = &quot;actual&quot;)<br>val [actual](actual.md): [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;?<br>The actual [PageRequest](../-page-request/index.md) associated with the request that resulted in this page being returned. This can be different from [original](original.md) if there was any formatting or altering of data by the source. |
| [info](info.md) | [common]<br>@SerialName(value = &quot;info&quot;)<br>open override val [info](info.md): [PageInfo](../-page-info/index.md)<br>The [PageInfo](../-page-info/index.md) associated with this [Page](../-page/index.md). |
| [items](items.md) | [common]<br>@SerialName(value = &quot;items&quot;)<br>open override val [items](items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>The [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of returned items of type T. |
| [original](original.md) | [common]<br>@SerialName(value = &quot;original&quot;)<br>val [original](original.md): [PageRequest](../-page-request/index.md)&lt;[Request](index.md), [Filter](index.md)&gt;? = null<br>The original [PageRequest](../-page-request/index.md) associated with the request that resulted in this page being returned. |
| [pageSourceId](page-source-id.md) | [common]<br>@SerialName(value = &quot;source_id&quot;)<br>open override val [pageSourceId](page-source-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A unique identifier value for the data source. This value is especially important when you have numerous data sources that are being merged into a single source (ex: using [PageCollection](../-page-collection/index.md)), as it provides a way to load the next page of data from the appropriate data source. |

## Functions

| Name | Summary |
|---|---|
| [loadItems](../-resolved-page/load-items.md) | [common]<br>open suspend override fun [loadItems](../-resolved-page/load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
