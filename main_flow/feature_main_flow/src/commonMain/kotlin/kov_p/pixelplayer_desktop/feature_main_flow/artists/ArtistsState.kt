package kov_p.pixelplayer_desktop.feature_main_flow.artists

import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistVo

sealed interface ArtistsState {
    data object Loading : ArtistsState
    data class Data(
        val artists: List<ArtistVo>,
    ) : ArtistsState
}
