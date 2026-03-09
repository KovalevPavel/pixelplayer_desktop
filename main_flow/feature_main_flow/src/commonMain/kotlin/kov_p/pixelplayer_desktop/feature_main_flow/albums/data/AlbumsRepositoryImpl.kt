package kov_p.pixelplayer_desktop.feature_main_flow.albums.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kov_p.core_network.get
import kov_p.core_network.post
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumVo
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository

class AlbumsRepositoryImpl(
    private val client: HttpClient,
) : AlbumsRepository {
    override val albums: MutableStateFlow<List<AlbumVo>?> = MutableStateFlow(null)

    override suspend fun getAllAlbums() {
        client.get<List<AlbumDto>>(path = "albums/all")
            .mapNotNull { dto ->
                AlbumVo(
                    id = dto.id ?: return@mapNotNull null,
                    title = dto.title.orEmpty(),
                    cover = dto.cover.orEmpty(),
                    artist = dto.artist.orEmpty(),
                    year = dto.year.orEmpty(),
                    tracks = dto.tracks?.size ?: 0,
                )
            }
            .let { list -> albums.value = list }
    }

    override suspend fun createAlbum(
        artist: String,
        title: String,
        cover: String,
        year: Int
    ): String {
        val payload = NewAlbumDto(
            artistId = artist,
            title = title,
            cover = cover,
            year = year,
        )
            .let(Json::encodeToString)
        return client.post<String>(
            path = "albums/new",
            payload = payload,
        )
    }

    override suspend fun deleteAlbum(albumId: String) {
        client.post<String>(
            path = "albums/remove",
            parameters = mapOf("album_id" to albumId),
        )
    }
}

@Serializable
private data class AlbumDto(
    @SerialName("id") val id: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("cover") val cover: String? = null,
    @SerialName("year") val year: String? = null,
    @SerialName("artist") val artist: String? = null,
    @SerialName("tracks") val tracks: List<TrackDto>? = null,
) {
    @Serializable
    data class TrackDto(
        @SerialName("id") val id: String? = null,
    )
}

@Serializable
private data class NewAlbumDto(
    @SerialName("artist_id") val artistId: String,
    @SerialName("title") val title: String,
    @SerialName("cover") val cover: String,
    @SerialName("year") val year: Int,
)
