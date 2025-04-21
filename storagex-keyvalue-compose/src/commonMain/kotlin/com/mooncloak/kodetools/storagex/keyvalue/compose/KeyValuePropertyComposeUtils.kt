package com.mooncloak.kodetools.storagex.keyvalue.compose

import com.mooncloak.kodetools.storagex.keyvalue.KeyValueProperty
import com.mooncloak.kodetools.storagex.keyvalue.KeyValuePropertyLoadingEvent
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Retrieves a [State] of the underlying [KeyValueProperty] value changes as [KeyValuePropertyLoadingEvent]s.
 */
@Composable
public fun <Value : Any> KeyValueProperty<Value>.loadingEventState(
    context: CoroutineContext = kotlin.coroutines.EmptyCoroutineContext
): State<KeyValuePropertyLoadingEvent<Value>> {
    val event = remember {
        mutableStateOf<KeyValuePropertyLoadingEvent<Value>>(KeyValuePropertyLoadingEvent.Loading)
    }

    LaunchedEffect(this) {
        event.value = KeyValuePropertyLoadingEvent.Loaded(value = this@loadingEventState.get())
    }

    return this.flow()
        .map { value -> KeyValuePropertyLoadingEvent.Loaded(value = value) }
        .collectAsState(
            initial = event.value,
            context = context
        )
}

/**
 * Retrieves a [State] of the underlying [KeyValueProperty] value changes starting with [initial].
 *
 * @param [initial] The initial [Value] to set for the state.
 */
@Composable
public fun <Value : Any> KeyValueProperty<Value>.state(
    initial: Value
): State<Value?> {
    val current = remember { mutableStateOf<Value?>(initial) }

    LaunchedEffect(initial) {
        current.value = this@state.get()

        this@state.flow()
            .collect { value ->
                current.value = value
            }
    }

    return current
}
