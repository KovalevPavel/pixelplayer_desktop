package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album

internal data class NewAlbumState(
    val artists: List<AvailableArtistVs>,
    val progress: Progress,
) {

    sealed interface Progress {
        data object None : Progress
        data object Circular : Progress
        data class Linear(
            val completed: Int,
            val total: Int,
        ) : Progress
    }

    companion object {
        val init = NewAlbumState(artists = listOf(), progress = Progress.Circular)
    }
}
