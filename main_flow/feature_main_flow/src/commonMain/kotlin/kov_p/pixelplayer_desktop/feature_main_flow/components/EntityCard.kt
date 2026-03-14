package kov_p.pixelplayer_desktop.feature_main_flow.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun EntityCard(
    title: String,
    subtitle: String,
    footer: String? = null,
    image: String,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Surface(
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(size = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp).height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                model = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                onError = {
                    println("Error coil:\n${it.result}")
                },
                error = {
                    Box(
                        modifier = Modifier.background(color = Color.LightGray).matchParentSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }

                },
            )

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    maxLines = 2,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = subtitle,
                    maxLines = 1,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis,
                )
                footer?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = footer,
                        maxLines = 1,
                        fontSize = 12.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            ActionsColumn(
                onEditClick = onEditClick,
                onRemoveClick = onRemoveClick,
            )
        }
    }
}

@Composable
private fun RowScope.ActionsColumn(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Column(
        modifier = modifier.align(Alignment.CenterVertically),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IconButton(
            enabled = false,
            onClick = onEditClick,
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }

        IconButton(
            onClick = onRemoveClick,
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }
    }
}
