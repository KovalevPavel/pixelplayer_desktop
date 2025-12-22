package kov_p.pixelplayer_desktop.feature_main_flow.albums

import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumVo

sealed interface AlbumsState {
    data object Loading : AlbumsState
    data class Data(
        val albums: List<AlbumVo>,
    ) : AlbumsState
}
