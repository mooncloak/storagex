//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[isValidAt](is-valid-at.md)

# isValidAt

[common]\
fun &lt;[T](is-valid-at.md)&gt; [CachedValue](-cached-value/index.md)&lt;[T](is-valid-at.md)&gt;.[isValidAt](is-valid-at.md)(instant: Instant): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Determines whether this [CachedValue](-cached-value/index.md) instance is still considered valid at the provided time [instant](is-valid-at.md).

#### Return

`true` if this [CachedValue](-cached-value/index.md) is still considered valid at the provided time [instant](is-valid-at.md), and therefore the data can still be used, otherwise `false`.
