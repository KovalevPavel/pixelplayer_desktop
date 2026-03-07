package kov_p.pixelplayer_desktop.feature_login.creds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_login.LoginRepository

class CredentialsInputViewModel(
    private val credentialsRepository: CredentialsRepository,
    private val loginRepository: LoginRepository,
) : ViewModel() {
    private val _eventsFlow = MutableSharedFlow<CredentialsInputEvent>()
    val eventsFlow: Flow<CredentialsInputEvent> = _eventsFlow

    fun handleAction(action: CredentialsInputAction) {
        when (action) {
            is CredentialsInputAction.Login -> {
                loginUser(login = action.login, password = action.password)
            }

            is CredentialsInputAction.ChangeEndpoint -> {
                changeEndpoint()
            }
        }
    }

    private fun loginUser(login: String, password: String) {
        launch(
            body = {
                CredentialsInputEvent.ShowFullScreenLoader(show = true).let(::emitEvent)
                delay(1000)

                val token = loginRepository.loginUser(login = login, password = password)
                if (token.isNotEmpty()) {
                    credentialsRepository.saveNewToken(token)
                    CredentialsInputEvent.NavigateToMainFlow.let(::emitEvent)
                }
            },
            onFailure = {
                CredentialsInputEvent.ShowFullScreenLoader(show = false).let(::emitEvent)
                CredentialsInputEvent.ShowError(it.message.orEmpty()).let(::emitEvent)
            },
        )
    }

    private fun changeEndpoint() {
        launch(
            body = {
                credentialsRepository.saveNewEndpoint(null)
                CredentialsInputEvent.NavigateToInit.let(::emitEvent)
            },
        )
    }

    private fun emitEvent(newEvent: CredentialsInputEvent) {
        viewModelScope.launch { _eventsFlow.emit(newEvent) }
    }
}
