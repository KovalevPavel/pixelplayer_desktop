package kov_p.pixelplayer_desktop.feature_login.init

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_login.LoginScreen
import kov_p.pixelplayer_desktop.feature_login.init.di.initModule
import org.koin.compose.module.rememberKoinModules
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun InitComposable(
    navController: NavController,
    onAuthorized: () -> Unit,
) {
    rememberKoinModules { listOf(initModule) }
    val viewModel = koinViewModel<InitViewModel>()

    LaunchedEffect(Unit) {
        InitLoginAction.CheckCredentials.let(viewModel::handleAction)
    }

    viewModel.eventsFlow.collectWithLifecycle { event ->
        when (event) {
            is InitLoginEvent.NavigateToStep -> {
                navController.navigate(event.step) {
                    if (navController.currentDestination?.route == LoginScreen.Init::class.qualifiedName) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            is InitLoginEvent.NavigateToMainFlow -> {
                onAuthorized()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
