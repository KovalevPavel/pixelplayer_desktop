package kov_p.pixelplayer_desktop.api_main_flow

internal sealed interface MainFlowEvent {
    data object NavigateToLoginFlow : MainFlowEvent
    data class ShowMainFlow(
        val baseUrl: String,
        val token: String,
    ) : MainFlowEvent
}
