package kov_p.pixelplayer_desktop.feature_main_flow.artists.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kov_p.pixelplayer_desktop.core_ui.CardList
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.artists.ArtistsAction
import kov_p.pixelplayer_desktop.feature_main_flow.artists.ArtistsState
import kov_p.pixelplayer_desktop.feature_main_flow.artists.ArtistsViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist.NewArtistDialog
import kov_p.pixelplayer_desktop.feature_main_flow.components.EntityCard
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ArtistsComposable() {
    val scope = LocalMainScope.current
    val viewModel: ArtistsViewModel = remember { scope.get() }
    val state = viewModel.state

    var newDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (state is ArtistsState.Data) {
                FloatingActionButton(
                    onClick = { newDialog = true },
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = null,
                    )
                }
            }
        },
    ) {
        AnimatedContent(
            targetState = viewModel.state,
            contentKey = { it::class },
        ) { st ->
            when (st) {
                is ArtistsState.Data -> {
                    ArtistsDataComposable(
                        state = st,
                        onEditClick = {},
                        onRemoveClick = { ArtistsAction.DeleteArtist(it).let(viewModel::handleAction) },
                    )
                }

                is ArtistsState.Loading -> {
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
        NewArtistDialog(removeFromComposition = { newDialog = false })
    }
}

@Composable
private fun ArtistsDataComposable(
    state: ArtistsState.Data,
    onEditClick: () -> Unit,
    onRemoveClick: (String) -> Unit,
) {
    CardList(modifier = Modifier.fillMaxSize()) {
        items(items = state.artists, key = { item -> item.id }) { item ->
            EntityCard(
                title = item.name,
                subtitle = "Albums: ${item.albums}",
                image = item.avatar,
                onEditClick = onEditClick,
                onRemoveClick = { onRemoveClick(item.id) },
            )
        }
    }
}
