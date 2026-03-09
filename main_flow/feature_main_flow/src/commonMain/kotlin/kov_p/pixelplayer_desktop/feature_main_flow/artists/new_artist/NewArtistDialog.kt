package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberDialogState
import kov_p.pixelplayer_desktop.core_ui.EditDialog
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist.di.NewArtistScope
import kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist.di.newArtistModule
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import pixelplayer_desktop.feature_main_flow.generated.resources.Res
import pixelplayer_desktop.feature_main_flow.generated.resources.new_artist

@Composable
fun NewArtistDialog(
    removeFromComposition: () -> Unit,
) {
    val koin = getKoin()

    val scope = remember {
        koin.loadModules(listOf(newArtistModule))
        koin.createScope<NewArtistScope>()
    }

    scope.linkTo(LocalMainScope.current)

    val viewModel: NewArtistViewModel = remember { scope.get() }
    DisposableEffect(Unit) {
        onDispose { scope.close() }
    }

    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is NewArtistEvent.CloseDialog -> removeFromComposition()
        }
    }

    EditDialog(
        title = stringResource(Res.string.new_artist),
        state = rememberDialogState(size = DpSize(width = 600.dp, height = 280.dp)),
        resizable = false,
        onClose = { removeFromComposition() },
    ) {
        NewArtistDialogContent(
            state = viewModel.state,
            onAction = viewModel::handleAction,
        )
    }
}
