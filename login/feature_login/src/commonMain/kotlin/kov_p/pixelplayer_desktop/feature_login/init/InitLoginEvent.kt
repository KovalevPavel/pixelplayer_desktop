package kov_p.pixelplayer_desktop.feature_login.init

sealed interface InitLoginEvent {
    data class NavigateToStep(val step: Any) : InitLoginEvent
    data object NavigateToMainFlow : InitLoginEvent
}
