package kov_p.pixelplayer_desktop.feature_login.creds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.core_ui.PixelInputField
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_login._di.LocalLoginScope
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.KoinExperimentalAPI
import pixelplayer_desktop.feature_login.generated.resources.Res
import pixelplayer_desktop.feature_login.generated.resources.change_endpoint
import pixelplayer_desktop.feature_login.generated.resources.login
import pixelplayer_desktop.feature_login.generated.resources.password

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)
@Composable
fun CredentialsInputComposable(
    onAuthorized: () -> Unit,
    onChangeEndpoint: () -> Unit,
) {
    val scope = LocalLoginScope.current
    val viewModel: CredentialsInputViewModel = remember { scope.get() }

    var isLoaderVisible by rememberSaveable { mutableStateOf(false) }
    var error: String? by remember { mutableStateOf(null) }

    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is CredentialsInputEvent.NavigateToInit -> {
                onChangeEndpoint()
            }

            is CredentialsInputEvent.NavigateToMainFlow -> {
                onAuthorized()
            }

            is CredentialsInputEvent.ShowFullScreenLoader -> {
                isLoaderVisible = event.show
            }

            is CredentialsInputEvent.ShowError -> {
                error = event.message
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            var login by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }

            val isButtonEnabled by remember {
                derivedStateOf { login.isNotEmpty() && password.isNotEmpty() }
            }

            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                PixelInputField(
                    value = login,
                    onValueChange = {
                        login = it
                        error = null
                    },
                    placeholder = stringResource(Res.string.login).lowercase(),
                )

                PixelInputField(
                    value = password,
                    onValueChange = {
                        password = it
                        error = null
                    },
                    placeholder = stringResource(Res.string.password).lowercase(),
                    visualTransformation = PasswordVisualTransformation(),
                )

                Text(
                    text = error.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isButtonEnabled,
                    onClick = {
                        CredentialsInputAction.Login(login = login, password = password)
                            .let(viewModel::handleAction)
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.login),
                    )
                }

                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        CredentialsInputAction.ChangeEndpoint.let(viewModel::handleAction)
                    },
                ) {
                    Text(
                        text = stringResource(Res.string.change_endpoint),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    if (isLoaderVisible) {
        FullScreenLoader()
    }
}
