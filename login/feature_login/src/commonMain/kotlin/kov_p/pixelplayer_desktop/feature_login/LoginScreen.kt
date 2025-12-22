package kov_p.pixelplayer_desktop.feature_login

import kotlinx.serialization.Serializable

interface LoginScreen {
    @Serializable
    data object Init : LoginScreen

    @Serializable
    data object EndpointInput : LoginScreen

    @Serializable
    data object CredentialsInput : LoginScreen
}
