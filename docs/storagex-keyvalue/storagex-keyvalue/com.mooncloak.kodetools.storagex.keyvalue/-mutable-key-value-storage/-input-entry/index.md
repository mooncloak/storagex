//[storagex-keyvalue](../../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../../index.md)/[MutableKeyValueStorage](../index.md)/[InputEntry](index.md)

# InputEntry

[common]\
interface [InputEntry](index.md)&lt;[Key](index.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [inputValue](input-value.md) | [common]<br>abstract val [inputValue](input-value.md): [MutableKeyValueStorage.InputValue](../-input-value/index.md)&lt;[Value](index.md)&gt; |
| [key](key.md) | [common]<br>abstract val [key](key.md): [Key](index.md) |

## Functions

| Name | Summary |
|---|---|
| [component1](component1.md) | [common]<br>open operator fun [component1](component1.md)(): [Key](index.md) |
| [component2](component2.md) | [common]<br>open operator fun [component2](component2.md)(): [MutableKeyValueStorage.InputValue](../-input-value/index.md)&lt;[Value](index.md)&gt; |
