package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album

internal data class NewAlbumState(
    val artists: List<AvailableArtistVs>,
    val isLoaderVisible: Boolean,
    val errorMsg: String?,
) {
    companion object {
        val init = NewAlbumState(artists = listOf(), isLoaderVisible = true, errorMsg = null)
    }
}
