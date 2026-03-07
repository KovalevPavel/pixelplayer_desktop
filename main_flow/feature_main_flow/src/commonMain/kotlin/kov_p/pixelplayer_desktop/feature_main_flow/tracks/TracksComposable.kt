package kov_p.pixelplayer_desktop.feature_main_flow.tracks

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TracksComposable() {
    Text(
        text = "tracks",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}
