//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[putAll](put-all.md)

# putAll

[common]\
suspend fun &lt;[Key](put-all.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](put-all.md)&gt; [MutableKeyValueStorage](-mutable-key-value-storage/index.md)&lt;[Key](put-all.md)&gt;.[putAll](put-all.md)(vararg entries: [MutableKeyValueStorage.InputEntry](-mutable-key-value-storage/-input-entry/index.md)&lt;[Key](put-all.md), [Value](put-all.md)&gt;)

Puts all the provided [entries](put-all.md) into this [MutableKeyValueStorage](-mutable-key-value-storage/index.md).

[common]\
suspend fun &lt;[Key](put-all.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](put-all.md)&gt; [MutableKeyValueStorage](-mutable-key-value-storage/index.md)&lt;[Key](put-all.md)&gt;.[putAll](put-all.md)(map: [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)&lt;[Key](put-all.md), [MutableKeyValueStorage.InputValue](-mutable-key-value-storage/-input-value/index.md)&lt;[Value](put-all.md)&gt;&gt;)

Puts all the entries from the provided [map](put-all.md) into this [MutableKeyValueStorage](-mutable-key-value-storage/index.md).

[common]\
suspend fun &lt;[Key](put-all.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](put-all.md)&gt; [MutableKeyValueStorage](-mutable-key-value-storage/index.md)&lt;[Key](put-all.md)&gt;.[putAll](put-all.md)(vararg pairs: [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[Key](put-all.md), [MutableKeyValueStorage.InputValue](-mutable-key-value-storage/-input-value/index.md)&lt;[Value](put-all.md)&gt;&gt;)

Puts all the provided [pairs](put-all.md) into this [MutableKeyValueStorage](-mutable-key-value-storage/index.md).

[common]\
suspend fun &lt;[Key](put-all.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](put-all.md)&gt; [MutableKeyValueStorage](-mutable-key-value-storage/index.md)&lt;[Key](put-all.md)&gt;.[putAll](put-all.md)(sequence: [Sequence](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)&lt;[MutableKeyValueStorage.InputEntry](-mutable-key-value-storage/-input-entry/index.md)&lt;[Key](put-all.md), [Value](put-all.md)&gt;&gt;)

Puts all the provided [sequence](put-all.md) into this [MutableKeyValueStorage](-mutable-key-value-storage/index.md).
