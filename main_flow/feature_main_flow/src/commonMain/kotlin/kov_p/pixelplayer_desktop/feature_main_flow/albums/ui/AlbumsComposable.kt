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
import androidx.compose.material3.Scaffold
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
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumAction
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsState
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.NewDialog
import kov_p.pixelplayer_desktop.feature_main_flow.components.EntityCard

@Composable
fun AlbumsComposable() {
    val scope = LocalMainScope.current
    val viewModel: AlbumsViewModel = remember { scope.get() }
    val state = viewModel.state

    var newDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (state is AlbumsState.Data) {
                FloatingActionButton(
                    onClick = {
                        newDialog = true
                    },
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
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
        NewDialog { needToRefresh ->
            newDialog = false
            if (needToRefresh) {
                AlbumAction.FetchAlbums.let(viewModel::handleAction)
            }
        }
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
    CardList(modifier = Modifier.fillMaxSize()) {
        items(items = state.albums, key = { it.id }) { item ->
            EntityCard(
                title = item.title,
                subtitle = "${item.artist} • ${item.year}",
                footer = "Tracks: ${item.tracks}",
                image = item.cover,
                onEditClick = {
                    dialog = true
                },
                onRemoveClick = { onDelete(item.id) },
            )
        }
    }

    if (dialog) {
        EditDialog(
            title = "Edit album",
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

                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {},
                    ) {
                        Text("Save")
                    }

                    Button(
                        onClick = cl,
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
