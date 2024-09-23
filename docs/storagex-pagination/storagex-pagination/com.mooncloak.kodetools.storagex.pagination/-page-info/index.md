//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[PageInfo](index.md)

# PageInfo

[common]\
@Serializable

data class [PageInfo](index.md)(val hasPrevious: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, val hasNext: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, val firstCursor: [Cursor](../-cursor/index.md)? = null, val lastCursor: [Cursor](../-cursor/index.md)? = null, val returnedCount: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null, val totalCount: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null)

Represents information associated with a paginated data source request.

## Constructors

| | |
|---|---|
| [PageInfo](-page-info.md) | [common]<br>constructor(hasPrevious: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, hasNext: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null, firstCursor: [Cursor](../-cursor/index.md)? = null, lastCursor: [Cursor](../-cursor/index.md)? = null, returnedCount: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null, totalCount: [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [firstCursor](first-cursor.md) | [common]<br>@SerialName(value = &quot;first_cursor&quot;)<br>val [firstCursor](first-cursor.md): [Cursor](../-cursor/index.md)? = null<br>The [Cursor](../-cursor/index.md) representing the first item in the returned list of items. |
| [hasNext](has-next.md) | [common]<br>@SerialName(value = &quot;has_next&quot;)<br>val [hasNext](has-next.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null<br>Whether there is a next page that can be loaded. `true` if there is a next page that can be loaded, `false` if there is not a next page, `null` if it is unknown. |
| [hasPrevious](has-previous.md) | [common]<br>@SerialName(value = &quot;has_previous&quot;)<br>val [hasPrevious](has-previous.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null<br>Whether there is a previous page that can be loaded. `true` if there is a previous page that can be loaded, `false` if there is not a previous page, `null` if it is unknown. |
| [lastCursor](last-cursor.md) | [common]<br>@SerialName(value = &quot;last_cursor&quot;)<br>val [lastCursor](last-cursor.md): [Cursor](../-cursor/index.md)? = null<br>The [Cursor](../-cursor/index.md) representing the last item in the returned list of items. |
| [returnedCount](returned-count.md) | [common]<br>@SerialName(value = &quot;count&quot;)<br>val [returnedCount](returned-count.md): [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null<br>The amount of items returned in this page. This is provided as a convenience and should always match the amount of items in the Page.items list. |
| [totalCount](total-count.md) | [common]<br>@SerialName(value = &quot;total&quot;)<br>val [totalCount](total-count.md): [UInt](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-u-int/index.html)? = null<br>The total amount of items that can be queried and paginated through, or `null` if the value is unknown. |
