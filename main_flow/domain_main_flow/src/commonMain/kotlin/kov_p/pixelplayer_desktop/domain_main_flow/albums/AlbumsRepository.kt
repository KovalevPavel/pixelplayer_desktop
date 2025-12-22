package kov_p.pixelplayer_desktop.domain_main_flow.albums

interface AlbumsRepository {
    suspend fun getAllAlbums(): List<AlbumVo>
    suspend fun createAlbum(
        artist: String,
        title: String,
        cover: String,
        year: Int,
    ): String

    suspend fun deleteAlbum(albumId: String)
}
