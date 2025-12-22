package kov_p.pixelplayer_desktop.feature_login.endpoint

internal sealed interface EndpointInputAction {
    @JvmInline
    value class CheckEndpoint(val endpoint: String) : EndpointInputAction
}
