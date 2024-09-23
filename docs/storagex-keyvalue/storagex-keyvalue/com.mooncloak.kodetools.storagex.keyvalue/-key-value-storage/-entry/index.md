//[storagex-keyvalue](../../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../../index.md)/[KeyValueStorage](../index.md)/[Entry](index.md)

# Entry

[common]\
interface [Entry](index.md)&lt;[Key](index.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [RawValue](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [key](key.md) | [common]<br>abstract val [key](key.md): [Key](index.md) |
| [storedValue](stored-value.md) | [common]<br>abstract val [storedValue](stored-value.md): [KeyValueStorage.StoredValue](../-stored-value/index.md)&lt;[RawValue](index.md)&gt; |

## Functions

| Name | Summary |
|---|---|
| [component1](component1.md) | [common]<br>open operator fun [component1](component1.md)(): [Key](index.md) |
| [component2](component2.md) | [common]<br>open operator fun [component2](component2.md)(): [KeyValueStorage.StoredValue](../-stored-value/index.md)&lt;[RawValue](index.md)&gt; |
