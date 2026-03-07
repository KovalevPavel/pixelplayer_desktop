package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album

sealed interface NewAlbumEvent {
    data object CloseDialog : NewAlbumEvent

    @JvmInline
    value class ShowError(val message: String) : NewAlbumEvent
}
