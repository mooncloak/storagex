//[storagex-keyvalue](../../index.md)/[com.mooncloak.kodetools.storagex.keyvalue](index.md)/[invoke](invoke.md)

# invoke

[common]\
operator fun [KeyValueStorage.StoredValue.Companion](-key-value-storage/-stored-value/-companion/index.md).[invoke](invoke.md)(rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), format: StringFormat): [KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;

operator fun &lt;[Value](invoke.md)&gt; [KeyValueStorage.StoredValue.Companion](-key-value-storage/-stored-value/-companion/index.md).[invoke](invoke.md)(rawValue: [Value](invoke.md), format: SerialFormat): [KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;[Value](invoke.md)&gt;

Creates a new [StoredValue](-key-value-storage/-stored-value/index.md) instance with the provided values.

[common]\
operator fun &lt;[Key](invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](invoke.md)&gt; [KeyValueStorage.Entry.Companion](-key-value-storage/-entry/-companion/index.md).[invoke](invoke.md)(key: [Key](invoke.md), storedValue: [KeyValueStorage.StoredValue](-key-value-storage/-stored-value/index.md)&lt;[Value](invoke.md)&gt;): [KeyValueStorage.Entry](-key-value-storage/-entry/index.md)&lt;[Key](invoke.md), [Value](invoke.md)&gt;

operator fun &lt;[Key](invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)&gt; [KeyValueStorage.Entry.Companion](-key-value-storage/-entry/-companion/index.md).[invoke](invoke.md)(key: [Key](invoke.md), rawValue: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), format: StringFormat): [KeyValueStorage.Entry](-key-value-storage/-entry/index.md)&lt;[Key](invoke.md), [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt;

operator fun &lt;[Key](invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](invoke.md)&gt; [KeyValueStorage.Entry.Companion](-key-value-storage/-entry/-companion/index.md).[invoke](invoke.md)(key: [Key](invoke.md), rawValue: [Value](invoke.md), format: SerialFormat): [KeyValueStorage.Entry](-key-value-storage/-entry/index.md)&lt;[Key](invoke.md), [Value](invoke.md)&gt;

Creates a new [KeyValueStorage.Entry](-key-value-storage/-entry/index.md) instance with the provided values.

[common]\
operator fun &lt;[Value](invoke.md)&gt; [MutableKeyValueStorage.InputValue.Companion](-mutable-key-value-storage/-input-value/-companion/index.md).[invoke](invoke.md)(value: [Value](invoke.md), serializer: SerializationStrategy&lt;[Value](invoke.md)&gt;): [MutableKeyValueStorage.InputValue](-mutable-key-value-storage/-input-value/index.md)&lt;[Value](invoke.md)&gt;

Creates a new [InputValue](-mutable-key-value-storage/-input-value/index.md) instance with the provided values.

[common]\
operator fun &lt;[Key](invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](invoke.md)&gt; [MutableKeyValueStorage.InputEntry.Companion](-mutable-key-value-storage/-input-entry/-companion/index.md).[invoke](invoke.md)(key: [Key](invoke.md), inputValue: [MutableKeyValueStorage.InputValue](-mutable-key-value-storage/-input-value/index.md)&lt;[Value](invoke.md)&gt;): [MutableKeyValueStorage.InputEntry](-mutable-key-value-storage/-input-entry/index.md)&lt;[Key](invoke.md), [Value](invoke.md)&gt;

operator fun &lt;[Key](invoke.md) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html), [Value](invoke.md)&gt; [MutableKeyValueStorage.InputEntry.Companion](-mutable-key-value-storage/-input-entry/-companion/index.md).[invoke](invoke.md)(key: [Key](invoke.md), value: [Value](invoke.md), serializer: SerializationStrategy&lt;[Value](invoke.md)&gt;): [MutableKeyValueStorage.InputEntry](-mutable-key-value-storage/-input-entry/index.md)&lt;[Key](invoke.md), [Value](invoke.md)&gt;

Creates a new [InputEntry](-mutable-key-value-storage/-input-entry/index.md) instance with the provided values.
