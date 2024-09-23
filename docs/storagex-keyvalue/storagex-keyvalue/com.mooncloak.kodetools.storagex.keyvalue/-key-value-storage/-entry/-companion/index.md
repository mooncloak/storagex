//[storagex-keyvalue](../../../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../../../index.md)/[KeyValueStorage](../../index.md)/[Entry](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [invoke](../../../invoke.md) | [common]<br>operator fun &lt;[Key](../../../invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](../../../invoke.md)&gt; [KeyValueStorage.Entry.Companion](index.md).[invoke](../../../invoke.md)(key: [Key](../../../invoke.md), storedValue: [KeyValueStorage.StoredValue](../../-stored-value/index.md)&lt;[Value](../../../invoke.md)&gt;): [KeyValueStorage.Entry](../index.md)&lt;[Key](../../../invoke.md), [Value](../../../invoke.md)&gt;<br>operator fun &lt;[Key](../../../invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](../../../invoke.md)&gt; [KeyValueStorage.Entry.Companion](index.md).[invoke](../../../invoke.md)(key: [Key](../../../invoke.md), rawValue: [Value](../../../invoke.md), format: SerialFormat): [KeyValueStorage.Entry](../index.md)&lt;[Key](../../../invoke.md), [Value](../../../invoke.md)&gt;<br>operator fun &lt;[Key](../../../invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage.Entry.Companion](index.md).[invoke](../../../invoke.md)(key: [Key](../../../invoke.md), rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), format: StringFormat): [KeyValueStorage.Entry](../index.md)&lt;[Key](../../../invoke.md), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;<br>Creates a new [KeyValueStorage.Entry](../index.md) instance with the provided values. |
