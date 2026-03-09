package kov_p.pixelplayer_desktop.domain_main_flow.artists

import kotlinx.coroutines.flow.StateFlow

interface ArtistsRepository {
    val artists: StateFlow<List<ArtistVo>?>

    suspend fun getAllArtists()
    suspend fun createNewArtist(name: String, avatarUrl: String): String
    suspend fun deleteArtist(artistId: String)
}
