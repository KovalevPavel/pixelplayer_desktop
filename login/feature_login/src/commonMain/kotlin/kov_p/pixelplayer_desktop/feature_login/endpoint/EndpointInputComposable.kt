package kov_p.pixelplayer_desktop.feature_login.endpoint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_login.LoginScreen
import kov_p.pixelplayer_desktop.feature_login._di.LocalLoginScope
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun EndpointInputComposable(
    navController: NavController,
) {
//    rememberKoinModules { listOf(endpointInputModule) }
//    val viewModel = koinViewModel<EndpointInputViewModel>()

    val scope = LocalLoginScope.current

    val viewModel: EndpointInputViewModel = remember { scope.get() }

    var endpoint by rememberSaveable { mutableStateOf("") }
    var isLoaderVisible by remember { mutableStateOf(false) }
    val isButtonEnabled by remember {
        derivedStateOf { endpoint.isNotEmpty() }
    }

    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is EndpointInputEvent.ShowFullScreenLoader -> {
                isLoaderVisible = event.show
            }

            is EndpointInputEvent.NavigateToCredentialsInput -> {
                navController.navigate(LoginScreen.CredentialsInput)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            TextField(
                value = endpoint,
                onValueChange = { endpoint = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (isButtonEnabled) {
                            EndpointInputAction.CheckEndpoint(endpoint).let(viewModel::handleAction)
                        }
                    },
                ),
                singleLine = true,
                placeholder = { Text(text = "https://") },
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = isButtonEnabled,
                onClick = {
                    EndpointInputAction.CheckEndpoint(endpoint).let(viewModel::handleAction)
                },
            ) {
                Text("Next")
            }
        }
    }

    if (isLoaderVisible) {
        FullScreenLoader()
    }
}
