package kov_p.pixelplayer_desktop.feature_login.creds

sealed interface CredentialsInputAction {
    data class Login(
        val login: String,
        val password: String,
    ) : CredentialsInputAction

    data object ChangeEndpoint : CredentialsInputAction
}
