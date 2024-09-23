//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PagePlaceholder](index.md)

# PagePlaceholder

[common]\
interface [PagePlaceholder](index.md)&lt;[Type](index.md)&gt; : [Page](../-page/index.md)&lt;[Type](index.md)&gt; 

Represents a placeholder for a [Page](../-page/index.md). For instance, when loading from a very large data set, it may be desirable to only store a certain amount of pages in memory at a time to prevent out-of-memory exceptions and slow applications. One way to continue to paginate from a large data set and support only the most recent pages in memory at a time, is to use a [PagePlaceholder](index.md), where the corresponding [Page](../-page/index.md) can be loaded when it needs to be displayed. The data associated with the [PagePlaceholder](index.md) may be persisted to disk, in-memory, or require an HTTP request to reload. How the data is stored and reloaded is an implementation specific detail.

!Note For supporting a very, very large data set, consider using out-of-order loading of pages instead of an in-order [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of [Page](../-page/index.md)s. That will allow completely removing older pages from memory when they are no longer needed.

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [pageCursor](page-cursor.md) | [common]<br>abstract val [pageCursor](page-cursor.md): [PageCursor](../-page-cursor/index.md)<br>A [PageCursor](../-page-cursor/index.md) which is an opaque [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value from the caller's perspective. From the implementor's perspective, this is a serialized object value containing data necessary for the (re-)loading of the page data. This value could be a serialized form of a [Cursor](../-cursor/index.md) and [Direction](../-direction/index.md), or contain more complex data, it is up to the implementation to decide what data to store. |

## Functions

| Name | Summary |
|---|---|
| [loadItems](../-page/load-items.md) | [common]<br>abstract suspend fun [loadItems](../-page/load-items.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Type](index.md)&gt;<br>Retrieves the underlying [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) of data items for this page. |
