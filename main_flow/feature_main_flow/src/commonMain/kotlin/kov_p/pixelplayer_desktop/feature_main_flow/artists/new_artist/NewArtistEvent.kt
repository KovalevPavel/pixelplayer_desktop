package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

internal sealed interface NewArtistEvent {
    data object CloseDialog : NewArtistEvent
}
