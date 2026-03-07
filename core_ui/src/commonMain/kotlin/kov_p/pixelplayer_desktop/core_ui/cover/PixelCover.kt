package kov_p.pixelplayer_desktop.core_ui.cover

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import kov_p.pixelplayer_desktop.core_ui.theme.PixelplayerTheme
import java.io.File

private val supportedImagesExtensions = listOf("jpg", "jpeg", "png")

@Composable
fun PixelCover(
    cover: CoverData?,
    onImageSelected: (CoverData.FileSystem) -> Unit,
) {
    var isPickerVisible by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.size(150.dp),
        onClick = { isPickerVisible = true },
        shape = RoundedCornerShape(10.dp),
    ) {
        SubcomposeAsyncImage(
            model = when (cover) {
                is CoverData.Binary -> {
                    cover.bytes
                }

                is CoverData.FileSystem -> {
                    File(cover.path)
                }

                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Click to select",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        )
                    }
                }
            },
            contentScale = ContentScale.Crop,
            contentDescription = null,
            loading = { Placeholder() },
            error = {
                if (cover != null) {
                    Placeholder(isError = true)
                }
            },
        )
    }

    RegisterFilePicker(
        isVisible = isPickerVisible,
        fileExtensions = supportedImagesExtensions,
        allowMultiple = false,
        onSelected = {
            isPickerVisible = false
            val path = it?.firstOrNull()?.path ?: return@RegisterFilePicker
            onImageSelected(CoverData.FileSystem(path))
        },
    )
}

@Composable
private fun BoxScope.Placeholder(isError: Boolean = false) {
    Box(modifier = Modifier.matchParentSize()) {
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
        )
    }
}

@Preview
@Composable
private fun CoverPreview() {
    PixelplayerTheme {
        PixelCover(cover = null, onImageSelected = {})
    }
}
