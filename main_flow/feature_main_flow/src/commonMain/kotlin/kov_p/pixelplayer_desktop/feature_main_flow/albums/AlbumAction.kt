package kov_p.pixelplayer_desktop.feature_main_flow.albums

sealed interface AlbumAction {
    @JvmInline
    value class DeleteAlbum(val albumId: String) : AlbumAction
}
