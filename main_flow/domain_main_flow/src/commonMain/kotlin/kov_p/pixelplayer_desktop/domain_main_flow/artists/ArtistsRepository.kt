package kov_p.pixelplayer_desktop.domain_main_flow.artists

interface ArtistsRepository {
    suspend fun getAllArtists(): List<ArtistVo>
    suspend fun createNewArtist(name: String, avatarUrl: String): String
    suspend fun deleteArtist(artistId: String)
}
