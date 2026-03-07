package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.core_ui.cover.CoverData
import kov_p.pixelplayer_desktop.core_ui.cover.PixelCover

@Composable
internal fun NewArtistDialogContent(
    modifier: Modifier = Modifier,
    state: NewArtistState,
    onAction: (NewArtistAction) -> Unit,
) {
    var newArtistName by rememberSaveable { mutableStateOf("") }
    var photo by rememberSaveable { mutableStateOf<CoverData.FileSystem?>(null) }

    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(all = 16.dp),
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
                textStyle = MaterialTheme.typography.bodyMedium,
                placeholder = {
                    Text(
                        text = "Enter artist's name",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                singleLine = true,
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.errorMsg.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            PixelCover(
                cover = photo,
                onImageSelected = { photo = it },
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = newArtistName.isNotEmpty() && photo != null,
                onClick = {
                    onAction(NewArtistAction.CreateArtist(name = newArtistName, avatar = photo?.path.orEmpty()))
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
