//[storagex-pagination](../../index.md)/[com.mooncloak.kodetools.storagex.pagination](index.md)/[invoke](invoke.md)

# invoke

[common]\
inline operator fun &lt;[Request](invoke.md), [Filter](invoke.md), [Type](invoke.md)&gt; [ResolvedPage.Companion](-resolved-page/-companion/index.md).[invoke](invoke.md)(pageSourceId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), items: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](invoke.md)&gt; = emptyList(), info: [PageInfo](-page-info/index.md) = PageInfo(), original: [PageRequest](-page-request/index.md)&lt;[Request](invoke.md), [Filter](invoke.md)&gt;? = null, actual: [PageRequest](-page-request/index.md)&lt;[Request](invoke.md), [Filter](invoke.md)&gt;? = original): [DefaultResolvedPage](-default-resolved-page/index.md)&lt;[Request](invoke.md), [Filter](invoke.md), [Type](invoke.md)&gt;

Creates a default [ResolvedPage](-resolved-page/index.md) instance wrapping the provided values.

[common]\
inline operator fun &lt;[Type](invoke.md)&gt; [PageCollection.Companion](-page-collection/-companion/index.md).[invoke](invoke.md)(pages: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ResolvedPage](-resolved-page/index.md)&lt;[Type](invoke.md)&gt;&gt; = emptyList()): [DefaultPageCollection](-default-page-collection/index.md)&lt;[Type](invoke.md)&gt;

Creates a default [PageCollection](-page-collection/index.md) instance wrapping the provided [pages](invoke.md).

[common]\
operator fun &lt;[Request](invoke.md), [Filters](invoke.md), [Result](invoke.md)&gt; [Pager.Companion](-pager/-companion/index.md).[invoke](invoke.md)(sources: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[PagedDataRepository](-paged-data-repository/index.md)&lt;[Request](invoke.md), [Filters](invoke.md), [Result](invoke.md)&gt;&gt;): [Pager](-pager/index.md)&lt;[Request](invoke.md), [Filters](invoke.md), [Result](invoke.md)&gt;
