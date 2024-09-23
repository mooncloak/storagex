//[storagex-keyvalue](../../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](../index.md)/[CachedValue](index.md)

# CachedValue

[common]\
@Serializable

data class [CachedValue](index.md)&lt;[T](index.md)&gt;(val timestamp: Instant, val expiresIn: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), val value: [T](index.md))

Represents the default internal representation of data stored within a Cache.

## Constructors

| | |
|---|---|
| [CachedValue](-cached-value.md) | [common]<br>constructor(timestamp: Instant, expiresIn: [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html), value: [T](index.md)) |

## Properties

| Name | Summary |
|---|---|
| [expiresIn](expires-in.md) | [common]<br>@SerialName(value = &quot;expires_in&quot;)<br>val [expiresIn](expires-in.md): [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html)<br>The expiration [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) of the data. |
| [timestamp](timestamp.md) | [common]<br>@SerialName(value = &quot;timestamp&quot;)<br>val [timestamp](timestamp.md): Instant<br>The Instant when the data was cached. |
| [value](value.md) | [common]<br>@SerialName(value = &quot;value&quot;)<br>val [value](value.md): [T](index.md)<br>The actual data. |

## Functions

| Name | Summary |
|---|---|
| [isValidAt](../is-valid-at.md) | [common]<br>fun &lt;[T](../is-valid-at.md)&gt; [CachedValue](index.md)&lt;[T](../is-valid-at.md)&gt;.[isValidAt](../is-valid-at.md)(instant: Instant): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Determines whether this [CachedValue](index.md) instance is still considered valid at the provided time [instant](../is-valid-at.md). |
