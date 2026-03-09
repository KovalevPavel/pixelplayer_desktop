package kov_p.pixelplayer_desktop.feature_main_flow._data

import kotlinx.coroutines.coroutineScope
import kov_p.pixelplayer_desktop.domain_main_flow.UpdateInfoInteractor
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository

class UpdateInfoInteractorImpl(
    private val albumsRepository: AlbumsRepository,
    private val artistsRepository: ArtistsRepository,
) : UpdateInfoInteractor {
    override suspend fun invoke() {
        coroutineScope { albumsRepository.getAllAlbums() }
        coroutineScope { artistsRepository.getAllArtists() }
    }
}
