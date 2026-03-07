package kov_p.pixelplayer_desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kov_p.pixelplayer_desktop.api_login.LoginFlow
import kov_p.pixelplayer_desktop.api_login.registerLoginFlow
import kov_p.pixelplayer_desktop.api_main_flow.MainFlow
import kov_p.pixelplayer_desktop.api_main_flow.registerMainFlow
import kov_p.pixelplayer_desktop.core_ui.theme.PixelplayerTheme
import org.koin.compose.KoinApplication

fun main() = application {
    PixelplayerTheme {
        Window(
            onCloseRequest = ::exitApplication,
            title = "pixelplayer_desktop",
            resizable = false,
        ) {
            val navController = rememberNavController()

            KoinApplication(
                application = { initDi() },
            ) {
                NavHost(
                    modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
                    navController = navController,
                    startDestination = LoginFlow,
                ) {
                    registerLoginFlow {
                        navController.navigate(MainFlow) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    registerMainFlow {
                        navController.navigate(LoginFlow) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
