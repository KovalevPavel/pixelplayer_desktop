package kov_p.pixelplayer_desktop.feature_main_flow.albums.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.core_ui.CardList
import kov_p.pixelplayer_desktop.core_ui.EditDialog
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumAction
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsEvent
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsState
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.NewDialog
import kov_p.pixelplayer_desktop.feature_main_flow.components.EntityCard
import org.jetbrains.compose.resources.stringResource
import pixelplayer_desktop.feature_main_flow.generated.resources.Res
import pixelplayer_desktop.feature_main_flow.generated.resources.album_card_footer
import pixelplayer_desktop.feature_main_flow.generated.resources.album_card_subtitle
import pixelplayer_desktop.feature_main_flow.generated.resources.cancel
import pixelplayer_desktop.feature_main_flow.generated.resources.edit_album_dialog_title
import pixelplayer_desktop.feature_main_flow.generated.resources.empty_albums
import pixelplayer_desktop.feature_main_flow.generated.resources.save

@Composable
fun AlbumsComposable() {
    val scope = LocalMainScope.current
    val viewModel: AlbumsViewModel = remember { scope.get() }
    val state = viewModel.state

    var newDialog by remember { mutableStateOf(false) }

    val snackHost = remember { SnackbarHostState() }
    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is AlbumsEvent.ShowError -> snackHost.showSnackbar(message = event.message)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (state is AlbumsState.Data) {
                FloatingActionButton(onClick = { newDialog = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackHost) },
    ) {
        AnimatedContent(
            targetState = state,
        ) { st ->
            when (st) {
                is AlbumsState.Data -> {
                    AlbumDataComposable(
                        state = st,
                        onEdit = {},
                        onDelete = { AlbumAction.DeleteAlbum(it).let(viewModel::handleAction) },
                    )
                }

                is AlbumsState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    if (newDialog) {
        NewDialog { newDialog = false }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlbumDataComposable(
    state: AlbumsState.Data,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit,
) {
    var dialog by rememberSaveable { mutableStateOf(false) }
    if (state.albums.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.empty_albums),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    } else {
        CardList(modifier = Modifier.fillMaxSize()) {
            items(items = state.albums, key = { it.id }) { item ->
                EntityCard(
                    title = item.title,
                    subtitle = stringResource(Res.string.album_card_subtitle, item.artist, item.year),
                    footer = stringResource(Res.string.album_card_footer, item.tracks),
                    image = item.cover,
                    onEditClick = {
                        dialog = true
                    },
                    onRemoveClick = { onDelete(item.id) },
                )
            }
        }
    }

    if (dialog) {
        EditDialog(
            title = stringResource(Res.string.edit_album_dialog_title),
            onClose = { dialog = false },
        ) { cl ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ) {
                    // todo
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {},
                    ) {
                        Text(
                            text = stringResource(Res.string.save),
                        )
                    }

                    Button(
                        onClick = cl,
                    ) {
                        Text(
                            text = stringResource(Res.string.cancel),
                        )
                    }
                }
            }
        }
    }
}
