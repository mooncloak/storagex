//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PageCollection](index.md)

# PageCollection

interface [PageCollection](index.md)&lt;[Type](index.md)&gt; : [Page](../-page/index.md)&lt;[Type](index.md)&gt; 

Represents a collection of [Page](../-page/index.md)s collectively representing a single loaded [Page](../-page/index.md). This could be useful for when paginating through multiple sources of data. For instance, consider a local source of one data set and a remote source of another data set, both of which are to be combined into a single data set. With this approach, a [PageCollection](index.md) can be used to represent a single [Page](../-page/index.md) from the combined data set, which contains a [Page](../-page/index.md) from each of the local and remote data sets. This way, the loading of the individual [Page](../-page/index.md)s is data source specific, but the overall pagination of the whole data can be represented with this [PageCursor](../-page-cursor/index.md) model.

#### Inheritors

| |
|---|
| [DefaultPageCollection](../-default-page-collection/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [pages](pages.md) | [common]<br>abstract val [pages](pages.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ResolvedPage](../-resolved-page/index.md)&lt;[Type](index.md)&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [loadItems](load-items.md) | [common]<br>open suspend override fun [loadItems](load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
