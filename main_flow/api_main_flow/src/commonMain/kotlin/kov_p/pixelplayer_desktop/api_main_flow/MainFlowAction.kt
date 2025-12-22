package kov_p.pixelplayer_desktop.api_main_flow

internal sealed interface MainFlowAction {
    data object Logout : MainFlowAction
}
