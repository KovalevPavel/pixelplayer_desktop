package kov_p.pixelplayer_desktop.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun <T> Flow<T>.collectWithLifecycle(
    key: Any? = Unit,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    initState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: suspend (T) -> Unit,
) {
    val scope = rememberCoroutineScope()

    DisposableEffect(key) {
        this@collectWithLifecycle.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = initState,
        )
            .onEach(collector)
            .launchIn(scope)

        onDispose { }
    }
}

fun Modifier.disableUserInput(): Modifier {
    return this.pointerInput(Unit) {}
}
