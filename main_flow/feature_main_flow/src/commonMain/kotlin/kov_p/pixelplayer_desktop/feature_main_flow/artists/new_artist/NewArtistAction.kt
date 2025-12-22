package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

internal sealed interface NewArtistAction {
    data class CreateArtist(
        val name: String,
        val avatar: String,
    ) : NewArtistAction
}
