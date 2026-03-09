package kov_p.pixelplayer_desktop.feature_main_flow.artists

sealed interface ArtistsEvent {
    @JvmInline
    value class ShowError(val message: String) : ArtistsEvent
}
