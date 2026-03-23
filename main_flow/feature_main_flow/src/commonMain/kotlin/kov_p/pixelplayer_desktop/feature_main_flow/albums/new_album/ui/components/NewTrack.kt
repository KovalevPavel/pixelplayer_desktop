package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import kov_p.pixelplayer_desktop.core_ui.PixelInputField
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumAction
import org.jetbrains.compose.resources.stringResource
import pixelplayer_desktop.feature_main_flow.generated.resources.Res
import pixelplayer_desktop.feature_main_flow.generated.resources.enter_displayer_track_title
import pixelplayer_desktop.feature_main_flow.generated.resources.track_pos

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
            text = stringResource(Res.string.track_pos, position),
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

            PixelInputField(
                modifier = Modifier.fillMaxWidth(),
                value = viewState.title,
                onValueChange = onTitleChanged,
                placeholder = stringResource(Res.string.enter_displayer_track_title),
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
                title = title,
                path = "/hardcoded/path/to/track.mp3",
                duration = null,
                bitrate = null,
                isLossless = false,
            ),
            onTitleChanged = { title = it },
            onRemove = {},
        )
    }
}
