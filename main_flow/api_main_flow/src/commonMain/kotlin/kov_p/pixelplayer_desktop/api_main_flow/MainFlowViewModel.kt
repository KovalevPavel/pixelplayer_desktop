package kov_p.pixelplayer_desktop.api_main_flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.core_ui.launch

internal class MainFlowViewModel(
    private val credentialsRepository: CredentialsRepository,
) : ViewModel() {
    private val _eventsFlow = MutableSharedFlow<MainFlowEvent>()
    val eventsFlow: Flow<MainFlowEvent> = _eventsFlow

    init {
        launch(
            body = {
                val event = MainFlowEvent.ShowMainFlow(
                    baseUrl = credentialsRepository.getSavedEndpoint() ?: error("No base url found"),
                    token = credentialsRepository.getSavedToken() ?: error("No token found"),
                )

                delay(1000)

                _eventsFlow.emit(event)
            },
        )
    }

    fun handleAction(action: MainFlowAction) {
        when (action) {
            MainFlowAction.Logout -> logout()
        }
    }

    private fun logout() {
        launch(
            body = {
                credentialsRepository.logout()
                _eventsFlow.emit(MainFlowEvent.NavigateToLoginFlow)
            },
        )
    }
}
