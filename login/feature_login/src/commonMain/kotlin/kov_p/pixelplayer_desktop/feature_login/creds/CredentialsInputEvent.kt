package kov_p.pixelplayer_desktop.feature_login.creds

sealed interface CredentialsInputEvent {
    @JvmInline
    value class ShowFullScreenLoader(val show: Boolean) : CredentialsInputEvent
    data object NavigateToInit : CredentialsInputEvent
    data object NavigateToMainFlow : CredentialsInputEvent
}
