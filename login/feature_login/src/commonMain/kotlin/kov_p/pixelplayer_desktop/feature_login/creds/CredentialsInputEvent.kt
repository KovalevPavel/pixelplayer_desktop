package kov_p.pixelplayer_desktop.feature_login.creds

sealed interface CredentialsInputEvent {
    @JvmInline
    value class ShowFullScreenLoader(val show: Boolean) : CredentialsInputEvent

    @JvmInline
    value class ShowError(val message: String) : CredentialsInputEvent

    data object NavigateToInit : CredentialsInputEvent
    data object NavigateToMainFlow : CredentialsInputEvent
}
