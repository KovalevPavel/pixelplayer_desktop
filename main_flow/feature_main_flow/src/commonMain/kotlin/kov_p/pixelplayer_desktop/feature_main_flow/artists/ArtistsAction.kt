package kov_p.pixelplayer_desktop.feature_main_flow.artists

sealed interface ArtistsAction {
    data object FetchData : ArtistsAction

    @JvmInline
    value class DeleteArtist(val artistId: String) : ArtistsAction
}
