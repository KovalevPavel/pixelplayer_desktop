package kov_p.pixelplayer_desktop.feature_login.init

internal sealed interface InitLoginAction {
    data object CheckCredentials : InitLoginAction
}
