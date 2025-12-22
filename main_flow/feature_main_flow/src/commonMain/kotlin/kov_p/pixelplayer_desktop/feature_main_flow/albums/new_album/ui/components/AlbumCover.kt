package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import kov_p.pixelplayer_desktop.core_ui.RegisterFilePicker
import java.io.File

@Composable
internal fun AlbumCover(
    path: String,
    onImageSelected: (String) -> Unit,
) {
    var isPickerVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.size(150.dp),
        onClick = { isPickerVisible = true },
        shape = RoundedCornerShape(10.dp),
    ) {
        SubcomposeAsyncImage(
            model = File(path),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            loading = { AlbumPlaceHolder() },
            error = { AlbumPlaceHolder(isError = true) },
        )
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color.Black.copy(alpha = .2f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Click to select",
                textAlign = TextAlign.Center,
                color = Color.White,
            )
        }
    }

    RegisterFilePicker(
        isVisible = isPickerVisible,
        fileExtensions = listOf("jpg", "jpeg"),
        allowMultiple = false,
        onSelected = {
            isPickerVisible = false
            val path = it?.firstOrNull()?.path ?: return@RegisterFilePicker
            onImageSelected(path)
        },
    )
}

@Composable
private fun BoxScope.AlbumPlaceHolder(isError: Boolean = false) {
    Box(
        modifier = Modifier.matchParentSize().background(color = Color.LightGray),
    ) {
        val icon = remember(isError) {
            if (isError) {
                Icons.Default.Error
            } else {
                Icons.Default.Downloading
            }
        }
        Icon(
            modifier = Modifier.size(48.dp).align(Alignment.Center),
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
        )
    }
}

@Preview
@Composable
private fun AlbumCoverPreview() {
    MaterialTheme {
        AlbumCover(path = "", onImageSelected = {})
    }
}
