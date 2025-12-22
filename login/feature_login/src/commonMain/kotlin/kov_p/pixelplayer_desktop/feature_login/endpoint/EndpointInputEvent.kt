package kov_p.pixelplayer_desktop.feature_login.endpoint

internal sealed interface EndpointInputEvent {
    @JvmInline
    value class ShowFullScreenLoader(val show: Boolean) : EndpointInputEvent
    data object NavigateToCredentialsInput : EndpointInputEvent
}
