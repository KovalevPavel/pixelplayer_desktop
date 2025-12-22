package kov_p.pixelplayer_desktop.core_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.DialogWindowScope
import androidx.compose.ui.window.rememberDialogState

@Composable
fun EditDialog(
    title: String,
    icon: Painter? = null,
    state: DialogState = rememberDialogState(),
    alwaysOnTop: Boolean = false,
    resizable: Boolean = false,
    onClose: () -> Unit,
    content: @Composable DialogWindowScope.(closeReq: () -> Unit) -> Unit,
) {
    DialogWindow(
        title = title,
        icon = icon,
        state = state,
        onCloseRequest = onClose,
        resizable = resizable,
        alwaysOnTop = alwaysOnTop,
    ) {
        content(onClose)
    }
}
