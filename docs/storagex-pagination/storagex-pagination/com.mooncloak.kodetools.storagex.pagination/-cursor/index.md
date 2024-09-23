//[storagex-pagination](../../../index.md)/[com.mooncloak.kodetools.storagex.pagination](../index.md)/[Cursor](index.md)

# Cursor

[common]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

@Serializable

value class [Cursor](index.md)(val value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))

Represents a generic pointer to an item in a paginated list. This serves as a reference to an item, so that subsequent requests can load items before or after that item.

## Constructors

| | |
|---|---|
| [Cursor](-cursor.md) | [common]<br>constructor(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>val [value](value.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The cursor [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value. This is an opaque [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) value from the perspective of the client, as it doesn't have to construct an instance of one, only use it as a reference. |

## Functions

| Name | Summary |
|---|---|
| [decode](../decode.md) | [common]<br>inline fun &lt;[DecodedCursor](../decode.md)&gt; [Cursor](index.md).[decode](../decode.md)(format: StringFormat): [DecodedCursor](../decode.md) |
