package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

data class NewArtistState(
    val isLoaderVisible: Boolean,
    val errorMsg: String?,
) {
    companion object {
        val init = NewArtistState(isLoaderVisible = false, errorMsg = null)
    }
}
