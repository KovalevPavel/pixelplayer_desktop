package kov_p.pixelplayer_desktop.core_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.darkrockstudios.libraries.mpfilepicker.MultipleFilePicker

@Composable
fun FullScreenLoader(
    backgroundColor: Color = MaterialTheme.colorScheme.scrim.copy(alpha = .8f),
) {
    Box(
        modifier = Modifier.disableUserInput().fillMaxSize().background(color = backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CardList(modifier: Modifier = Modifier, builder: LazyListScope.() -> Unit) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
    ) {
        builder()
    }
}

@Composable
fun RegisterFilePicker(
    isVisible: Boolean,
    fileExtensions: List<String>,
    allowMultiple: Boolean = false,
    onSelected: (List<MPFile<Any>>?) -> Unit,
) {
    var filePicker by remember(isVisible) { mutableStateOf(isVisible) }

    if (allowMultiple) {
        MultipleFilePicker(
            show = filePicker,
            fileExtensions = fileExtensions,
            title = "Pick files",
            initialDirectory = System.getProperty("user.home"),
        ) { files ->
            filePicker = false
            onSelected(files?.filter { it.path.substringAfterLast('.') in fileExtensions })
        }
    } else {
        FilePicker(
            show = filePicker,
            fileExtensions = fileExtensions,
            title = "Pick file",
            initialDirectory = System.getProperty("user.home"),
        ) { file ->
            filePicker = false
            onSelected(
                listOfNotNull(file).filter {
                    it.path.substringAfterLast('.') in fileExtensions
                },
            )
        }
    }
}

@Composable
fun PixelInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    readonly: Boolean = false,
    placeholder: String? = null,
    error: (() -> String?)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            textStyle = MaterialTheme.typography.bodyMedium,
            onValueChange = onValueChange,
            readOnly = readonly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            placeholder = placeholder?.let {
                {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            },
            isError = error?.invoke().orEmpty() != "",
            trailingIcon = trailingIcon,
        )

        error?.let { errorProvider ->
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = errorProvider().orEmpty(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}
