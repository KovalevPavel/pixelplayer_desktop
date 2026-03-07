package kov_p.pixelplayer_desktop.api_main_flow

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.serialization.Serializable
import kov_p.pixelplayer_desktop.api_main_flow.di.MainFlowScope
import kov_p.pixelplayer_desktop.api_main_flow.di.MainViewModelScope
import kov_p.pixelplayer_desktop.api_main_flow.di.mainFlowModule
import kov_p.pixelplayer_desktop.api_main_flow.di.mainViewModelScope
import kov_p.pixelplayer_desktop.core_ui.FullScreenLoader
import kov_p.pixelplayer_desktop.core_ui.collectWithLifecycle
import kov_p.pixelplayer_desktop.feature_main_flow.MainFlowScreen
import kov_p.pixelplayer_desktop.feature_main_flow._di.LocalMainScope
import kov_p.pixelplayer_desktop.feature_main_flow.albums.ui.AlbumsComposable
import kov_p.pixelplayer_desktop.feature_main_flow.artists.ui.ArtistsComposable
import kov_p.pixelplayer_desktop.feature_main_flow.tracks.TracksComposable
import org.koin.compose.getKoin
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
data object MainFlow

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.registerMainFlow(
    onLogout: () -> Unit,
) {
    composable<MainFlow> {
        val koin = getKoin()
        val scope = remember {
            koin.loadModules(listOf(mainViewModelScope))
            koin.createScope<MainViewModelScope>()
        }
        DisposableEffect(Unit) {
            onDispose { scope.close() }
        }

        val mainViewModel: MainFlowViewModel = remember { scope.get() }
        var creds: CredsWrapper? by remember { mutableStateOf(null) }

        mainViewModel.eventsFlow.collectWithLifecycle { event ->
            when (event) {
                is MainFlowEvent.NavigateToLoginFlow -> {
                    onLogout()
                }

                is MainFlowEvent.ShowMainFlow -> {
                    creds = CredsWrapper(baseUrl = event.baseUrl, token = event.token)
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
            MainFlowComposableWrapper(
                modifier = Modifier.matchParentSize(),
                creds = creds,
                onLogout = { MainFlowAction.Logout.let(mainViewModel::handleAction) },
            )
        }
    }
}

@Composable
private fun MainFlowComposableWrapper(
    modifier: Modifier,
    creds: CredsWrapper?,
    onLogout: () -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = creds,
    ) { c ->
        when (c) {
            null -> {
                FullScreenLoader()
            }

            else -> {
                MainFlowComposable(
                    baseUrl = c.baseUrl,
                    token = c.token,
                    onLogout = onLogout,
                )
            }
        }
    }
}

@Composable
private fun MainFlowComposable(
    baseUrl: String,
    token: String,
    onLogout: () -> Unit,
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add { chain ->
                    when (chain.request.data) {
                        is String -> {
                            val request = ImageRequest.Builder(chain.request)
                                .httpHeaders(NetworkHeaders.Builder().set("Authorization", token).build())
                                .data("$baseUrl/img/${chain.request.data}").build()
                            chain.withRequest(request).proceed()
                        }

                        else -> {
                            chain.proceed()
                        }
                    }
                }
            }
            .build()
    }
    val koin = getKoin()
    val scope = remember {
        val mainFlowModule = mainFlowModule(baseUrl = baseUrl, token = token)
        koin.loadModules(listOf(mainFlowModule))
        koin.createScope<MainFlowScope>()
    }

    DisposableEffect(Unit) {
        onDispose { scope.close() }
    }

    val mainFlowNavController = rememberNavController()
    var activeTab by rememberSaveable { mutableStateOf(MainFlowScreen.entries.first()) }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxHeight().weight(1f).padding(all = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MainFlowScreen.entries.forEach { screen ->
                TextButton(
                    enabled = activeTab != screen,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    onClick = {
                        mainFlowNavController.navigate(screen.name) {
                            popUpTo(0) { inclusive = true }
                        }
                        activeTab = screen
                    },
                ) {
                    Text(
                        text = screen.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (activeTab != screen) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RectangleShape,
                onClick = onLogout,
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Spacer(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.error)
                .fillMaxHeight()
                .width(1.dp),
        )

        CompositionLocalProvider(
            LocalMainScope provides scope,
        ) {
            NavHost(
                modifier = Modifier.fillMaxHeight().weight(4f).padding(all = 8.dp),
                navController = mainFlowNavController,
                startDestination = MainFlowScreen.entries.first().name,
            ) {

                MainFlowScreen.entries.forEach { screen ->
                    composable(route = screen.name) {
                        when (screen) {
                            MainFlowScreen.Artists -> {
                                ArtistsComposable()
                            }

                            MainFlowScreen.Albums -> {
                                AlbumsComposable()
                            }

                            MainFlowScreen.Tracks -> {
                                TracksComposable()
                            }
                        }
                    }
                }
            }
        }
    }
}
