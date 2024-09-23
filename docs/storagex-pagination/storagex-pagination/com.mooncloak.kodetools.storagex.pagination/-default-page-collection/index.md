//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[DefaultPageCollection](index.md)

# DefaultPageCollection

[common]\
@Serializable

@SerialName(value = &quot;collection&quot;)

data class [DefaultPageCollection](index.md)&lt;[Type](index.md)&gt;(val pages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ResolvedPage](../-resolved-page/index.md)&lt;[Type](index.md)&gt;&gt;) : [PageCollection](../-page-collection/index.md)&lt;[Type](index.md)&gt;

## Constructors

| | |
|---|---|
| [DefaultPageCollection](-default-page-collection.md) | [common]<br>constructor(pages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ResolvedPage](../-resolved-page/index.md)&lt;[Type](index.md)&gt;&gt;) |

## Properties

| Name | Summary |
|---|---|
| [pages](pages.md) | [common]<br>@SerialName(value = &quot;pages&quot;)<br>open override val [pages](pages.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ResolvedPage](../-resolved-page/index.md)&lt;[Type](index.md)&gt;&gt; |

## Functions

| Name | Summary |
|---|---|
| [loadItems](../-page-collection/load-items.md) | [common]<br>open suspend override fun [loadItems](../-page-collection/load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
