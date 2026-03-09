package kov_p.pixelplayer_desktop.domain_main_flow.albums

import kotlinx.coroutines.flow.StateFlow

interface AlbumsRepository {
    val albums: StateFlow<List<AlbumVo>?>

    suspend fun getAllAlbums()
    suspend fun createAlbum(
        artist: String,
        title: String,
        cover: String,
        year: Int,
    ): String

    suspend fun deleteAlbum(albumId: String)
}
