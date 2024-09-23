//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[ResolvedPage](index.md)

# ResolvedPage

interface [ResolvedPage](index.md)&lt;[Type](index.md)&gt; : [Page](../-page/index.md)&lt;[Type](index.md)&gt; 

Represents a resolved [Page](../-page/index.md) of data.

#### Inheritors

| |
|---|
| [DefaultResolvedPage](../-default-resolved-page/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [info](info.md) | [common]<br>abstract val [info](info.md): [PageInfo](../-page-info/index.md)<br>The [PageInfo](../-page-info/index.md) for this page of data. |
| [items](items.md) | [common]<br>abstract val [items](items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>The [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
| [pageSourceId](page-source-id.md) | [common]<br>abstract val [pageSourceId](page-source-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A unique identifier value for the data source. This value is especially important when you have numerous data sources that are being merged into a single source (ex: using [PageCollection](../-page-collection/index.md)), as it provides a way to load the next page of data from the appropriate data source. |

## Functions

| Name | Summary |
|---|---|
| [loadItems](load-items.md) | [common]<br>open suspend override fun [loadItems](load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
