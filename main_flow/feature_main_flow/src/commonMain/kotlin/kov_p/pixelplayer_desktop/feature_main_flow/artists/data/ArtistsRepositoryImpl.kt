package kov_p.pixelplayer_desktop.feature_main_flow.artists.data

import io.ktor.client.HttpClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kov_p.core_network.get
import kov_p.core_network.post
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistVo
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository

class ArtistsRepositoryImpl(
    private val client: HttpClient,
) : ArtistsRepository {
    override suspend fun getAllArtists(): List<ArtistVo> {
        return client.get<List<ArtistDto>>(path = "artists/all").mapNotNull { dto ->
            ArtistVo(
                id = dto.id ?: return@mapNotNull null,
                name = dto.name.orEmpty(),
                avatar = dto.avatar.orEmpty(),
                albums = dto.albums?.size ?: 0,
            )
        }
    }

    override suspend fun createNewArtist(name: String, avatarUrl: String): String {
        return client.post<String>(
            path = "artists/new",
            payload = NewArtistDto(name = name, avatar = avatarUrl).let(Json::encodeToString),
        )
    }

    override suspend fun deleteArtist(artistId: String) {
        client.post<String>(
            path = "artists/remove",
            parameters = mapOf("artist_id" to artistId),
        )
    }
}

@Serializable
private data class ArtistDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("avatar_url") val avatar: String? = null,
    @SerialName("albums") val albums: List<AlbumDto>? = null,
) {
    @Serializable
    data class AlbumDto(
        @SerialName("id") val id: String? = null,
    )
}

@Serializable
private data class NewArtistDto(
    @SerialName("name") val name: String,
    @SerialName("avatar_url") val avatar: String,
)
