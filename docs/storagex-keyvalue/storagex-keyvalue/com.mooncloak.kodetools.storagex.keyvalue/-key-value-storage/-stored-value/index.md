//[storagex-keyvalue](../../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../../index.md)/[KeyValueStorage](../index.md)/[StoredValue](index.md)

# StoredValue

[common]\
interface [StoredValue](index.md)&lt;[RawValue](index.md)&gt;

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [common]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [format](format.md) | [common]<br>abstract val [format](format.md): SerialFormat |
| [rawValue](raw-value.md) | [common]<br>abstract val [rawValue](raw-value.md): [RawValue](index.md) |

## Functions

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>abstract fun &lt;[Value](value.md)&gt; [value](value.md)(deserializer: DeserializationStrategy&lt;[Value](value.md)&gt;): [Value](value.md) |
| [value](../../value.md) | [common]<br>inline fun &lt;[Value](../../value.md)&gt; [KeyValueStorage.StoredValue](index.md)&lt;*&gt;.[value](../../value.md)(): [Value](../../value.md) |
