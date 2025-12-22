package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumAction

@Composable
internal fun NewTrack(
    modifier: Modifier = Modifier,
    position: Int,
    viewState: NewAlbumAction.NewTrack,
    onTitleChanged: (String) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.size(24.dp),
            text = "$position",
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
                text = viewState.path,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewState.title,
                onValueChange = onTitleChanged,
                placeholder = {
                    Text(text = "Enter displayed track title")
                },
            )
        }

        IconButton(onClick = onRemove) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun NewTrackPreview() {
    MaterialTheme {
        var title by remember { mutableStateOf("") }

        NewTrack(
            position = 1,
            viewState = NewAlbumAction.NewTrack(
                title = title, path = "/hardcoded/path/to/track.mp3",
            ),
            onTitleChanged = { title = it },
            onRemove = {},
        )
    }
}
