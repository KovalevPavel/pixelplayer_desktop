package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components.AlbumCover

@Composable
internal fun NewArtistDialogContent(
    modifier: Modifier = Modifier,
    state: NewArtistState,
    onAction: (NewArtistAction) -> Unit,
) {
    var newArtistName by rememberSaveable { mutableStateOf("") }
    var photo by rememberSaveable { mutableStateOf("") }

    val isButtonEnabled by remember {
        derivedStateOf { newArtistName.isNotEmpty() && photo.isNotEmpty() }
    }

    Row(
        modifier = modifier.height(IntrinsicSize.Min).padding(all = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = newArtistName,
                onValueChange = { newArtistName = it },
                placeholder = { Text("Enter artist's name") },
                singleLine = true,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.errorMsg.orEmpty(),
                color = Color.Red,
            )
        }

        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            AlbumCover(
                path = photo,
                onImageSelected = { photo = it },
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isButtonEnabled,
                onClick = {
                    onAction(NewArtistAction.CreateArtist(name = newArtistName, avatar = photo))
                },
            ) {
                Text(text = "Create artist")
            }
        }
    }

    if (state.isLoaderVisible) {
        FullScreenLoader()
    }
}
