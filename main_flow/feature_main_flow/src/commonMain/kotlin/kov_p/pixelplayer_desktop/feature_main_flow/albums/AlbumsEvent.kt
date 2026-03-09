package kov_p.pixelplayer_desktop.feature_main_flow.albums

sealed interface AlbumsEvent {
    @JvmInline
    value class ShowError(val message: String) : AlbumsEvent
}
