package kov_p.pixelplayer_desktop.feature_login.endpoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_login.LoginRepository

internal class EndpointInputViewModel(
    private val credentialsRepository: CredentialsRepository,
    private val loginRepository: LoginRepository,
) : ViewModel() {
    private val _eventsFlow = MutableSharedFlow<EndpointInputEvent>()
    val eventsFlow: Flow<EndpointInputEvent> = _eventsFlow

    fun handleAction(action: EndpointInputAction) {
        when (action) {
            is EndpointInputAction.CheckEndpoint -> checkEndpoint(endpoint = action.endpoint)
        }
    }

    private fun checkEndpoint(endpoint: String) {
        launch(
            body = {
                val resultEndpoint = if (endpoint == "localhost") FALLBACK_LOCALHOST_ENDPOINT else endpoint
                EndpointInputEvent.ShowFullScreenLoader(show = true).let(::emitNewEvent)
                delay(500)
                if (loginRepository.checkEndpoint(endpoint = resultEndpoint)) {
                    credentialsRepository.logout()
                    credentialsRepository.saveNewEndpoint(resultEndpoint)
                    EndpointInputEvent.NavigateToCredentialsInput.let(::emitNewEvent)
                }
            },
            finally = {
                EndpointInputEvent.ShowFullScreenLoader(show = false).let(::emitNewEvent)
            }
        )
    }

    private fun emitNewEvent(newEvent: EndpointInputEvent) {
        viewModelScope.launch { _eventsFlow.emit(newEvent) }
    }

    companion object {
        private const val FALLBACK_LOCALHOST_ENDPOINT = "http://localhost"
    }
}
