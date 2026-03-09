package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.core_ui.PixelInputField
import kov_p.pixelplayer_desktop.core_ui.cover.CoverData
import kov_p.pixelplayer_desktop.core_ui.cover.PixelCover
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.AvailableArtistVs
import org.jetbrains.compose.resources.stringResource
import pixelplayer_desktop.feature_main_flow.generated.resources.Res
import pixelplayer_desktop.feature_main_flow.generated.resources.click_to_select_artist
import pixelplayer_desktop.feature_main_flow.generated.resources.enter_albums_title
import pixelplayer_desktop.feature_main_flow.generated.resources.enter_albums_year

@Composable
internal fun DialogHeader(
    selectedArtist: AvailableArtistVs?,
    artists: List<AvailableArtistVs>,
    albumName: String,
    cover: CoverData?,
    albumYear: String,
    onArtistSelect: (AvailableArtistVs) -> Unit,
    onAlbumNameChanged: (String) -> Unit,
    onCoverSelected: (CoverData) -> Unit,
    onYearChanged: (String) -> Unit,
) {

    var isExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.height(IntrinsicSize.Min).widthIn(min = 450.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().weight(1f),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            var width by remember { mutableStateOf(0.dp) }
            val density = LocalDensity.current
            Column {
                PixelInputField(
                    modifier = Modifier.fillMaxWidth().onSizeChanged {
                        density.run { width = it.width.toDp() }
                    },
                    value = selectedArtist?.name.orEmpty(),
                    readonly = true,
                    placeholder = stringResource(Res.string.click_to_select_artist),
                    onValueChange = {},
                    trailingIcon = {
                        IconButton(onClick = { isExpanded = !isExpanded }) {
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                )

                DropdownMenu(
                    modifier = Modifier.width(width = width).heightIn(max = 200.dp),
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    scrollState = rememberScrollState(),
                ) {
                    artists.forEach { art ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = art.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            onClick = {
                                onArtistSelect(art)
                                isExpanded = false
                            },
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                PixelInputField(
                    modifier = Modifier.weight(2f),
                    value = albumName,
                    onValueChange = onAlbumNameChanged,
                    placeholder = stringResource(Res.string.enter_albums_title),
                    singleLine = true,
                )

                PixelInputField(
                    modifier = Modifier.weight(1f),
                    value = albumYear,
                    onValueChange = onYearChanged,
                    placeholder = stringResource(Res.string.enter_albums_year),
                    singleLine = true,
                )
            }
        }

        PixelCover(
            cover = cover,
            onImageSelected = onCoverSelected,
        )
    }
}

@Preview
@Composable
private fun DialogHeaderPreview() {
    var albumName by remember { mutableStateOf("") }

    MaterialTheme {
        DialogHeader(
            selectedArtist = null,
            artists = listOf(),
            albumName = albumName,
            onAlbumNameChanged = { albumName = it },
            cover = null,
            albumYear = "2007",
            onCoverSelected = {},
            onArtistSelect = {},
            onYearChanged = {},
        )
    }
}
