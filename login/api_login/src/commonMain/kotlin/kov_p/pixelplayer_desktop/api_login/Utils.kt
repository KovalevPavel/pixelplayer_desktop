package kov_p.pixelplayer_desktop.api_login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import kov_p.pixelplayer_desktop.feature_login.LoginScreen
import kov_p.pixelplayer_desktop.feature_login._di.LocalLoginScope
import kov_p.pixelplayer_desktop.feature_login._di.LoginScope
import kov_p.pixelplayer_desktop.feature_login._di.loginModule
import kov_p.pixelplayer_desktop.feature_login.creds.CredentialsInputComposable
import kov_p.pixelplayer_desktop.feature_login.endpoint.EndpointInputComposable
import kov_p.pixelplayer_desktop.feature_login.init.InitComposable
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object LoginFlow

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.registerLoginFlow(
    onAuthorized: () -> Unit,
) {
    composable<LoginFlow> {
        val koin = getKoin()
        val loginScope = remember {
            koin.loadModules(listOf(loginModule))
            koin.createScope<LoginScope>()
        }

        DisposableEffect(Unit) {
            onDispose { loginScope.close() }
        }

        val loginNavController = rememberNavController()

        CompositionLocalProvider(
            LocalLoginScope provides loginScope,
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navController = loginNavController,
                startDestination = LoginScreen.Init,
            ) {
                composable<LoginScreen.Init> {
                    InitComposable(
                        navController = loginNavController,
                        onAuthorized = onAuthorized,
                    )
                }

                composable<LoginScreen.EndpointInput> {
                    EndpointInputComposable(navController = loginNavController)
                }

                composable<LoginScreen.CredentialsInput> {
                    CredentialsInputComposable(
                        onAuthorized = onAuthorized,
                        onChangeEndpoint = {
                            loginNavController.navigate(LoginScreen.Init) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                    )
                }
            }
        }
    }
}
