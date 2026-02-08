package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album

import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.AlbumCover
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.DisksList

internal sealed interface NewAlbumAction {
    data object LoadAvailableArtists : NewAlbumAction
    data class CreateAlbum(
        val artistId: String,
        val albumName: String,
        val cover: AlbumCover,
        val year: Int,
        val disks: DisksList,
    ) : NewAlbumAction

    data class NewTrack(
        val title: String,
        val path: String,
    ) {
        val isFilled get() = title.isNotEmpty() && path.isNotEmpty()
    }
}
