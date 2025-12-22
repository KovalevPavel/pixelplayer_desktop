package kov_p.pixelplayer_desktop.feature_login.init

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.feature_login.LoginScreen

internal class InitViewModel(
    private val credentialsRepository: CredentialsRepository,
) : ViewModel() {
    private val _eventsFlow = MutableSharedFlow<InitLoginEvent>()
    val eventsFlow: Flow<InitLoginEvent> = _eventsFlow

    fun handleAction(action: InitLoginAction) {
        when (action) {
            is InitLoginAction.CheckCredentials -> {
                checkCredentials()
            }
        }
    }

    private fun checkCredentials() {
        launch(
            body = {
                credentialsRepository.getSavedToken()?.let {
                    val event = InitLoginEvent.NavigateToMainFlow
                    _eventsFlow.emit(event)
                    return@launch
                }

                val endpoint = credentialsRepository.getSavedEndpoint()
                    ?.takeIf(String::isNotEmpty)

                val navigationEvent = when (endpoint) {
                    null -> LoginScreen.EndpointInput
                    else -> LoginScreen.CredentialsInput
                }
                    .let(InitLoginEvent::NavigateToStep)

                _eventsFlow.emit(navigationEvent)
            },
        )
    }
}
