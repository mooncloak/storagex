//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[Page](index.md)

# Page

sealed interface [Page](index.md)&lt;[Type](index.md)&gt;

Represents a generic page of data. A [Page](index.md) can be a resolved page of data which contains the actual page data (via [ResolvedPage](../-resolved-page/index.md)), a collection of resolved pages (via [PageCollection](../-page-collection/index.md)), or a placeholder of data which can be lazily fetched (via [PagePlaceholder](../-page-placeholder/index.md)).

#### Inheritors

| |
|---|
| [ResolvedPage](../-resolved-page/index.md) |
| [PageCollection](../-page-collection/index.md) |
| [PagePlaceholder](../-page-placeholder/index.md) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [loadItems](load-items.md) | [common]<br>abstract suspend fun [loadItems](load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
