package kov_p.pixelplayer_desktop.core_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun ViewModel.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    body: suspend CoroutineScope.() -> Unit,
    onFailure: (Throwable) -> Unit = {},
    finally: () -> Unit = {},
) {
    val handler = CoroutineExceptionHandler { _, throwable ->
        onFailure(throwable)
        finally()
    }

    viewModelScope.launch(context + handler) {
        body()
        finally()
    }
}
