package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import io.ktor.utils.io.readFully
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.upload.UploadRepository
import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui.DisksList
import java.io.File
import java.util.UUID
import kotlin.io.deleteRecursively
import kotlin.io.inputStream
import kotlin.io.outputStream
import kotlin.use

internal class NewAlbumViewModel(
    private val artistsRepository: ArtistsRepository,
    private val albumsRepository: AlbumsRepository,
    private val uploadRepository: UploadRepository,
) : ViewModel() {
    var state: NewAlbumState by mutableStateOf(NewAlbumState.init)
        private set

    private val _eventsFlow = MutableSharedFlow<NewAlbumEvent>()
    val eventsFlow: Flow<NewAlbumEvent> = _eventsFlow

    init {
        NewAlbumAction.LoadAvailableArtists.let(::handleAction)
    }

    fun handleAction(action: NewAlbumAction) {
        when (action) {
            is NewAlbumAction.CreateAlbum -> {
                createAlbum(
                    artistId = action.artistId,
                    albumName = action.albumName,
                    cover = action.cover,
                    year = action.year,
                    disks = action.disks,
                )
            }

            is NewAlbumAction.LoadAvailableArtists -> {
                fetchAvailableArtists()
            }
        }
    }

    private fun fetchAvailableArtists() {
        launch(
            body = {
                val artists = artistsRepository.getAllArtists().map { AvailableArtistVs(id = it.id, name = it.name) }

                state = state.copy(artists = artists, isLoaderVisible = false)
            },
        )
    }

    private fun createAlbum(
        artistId: String,
        albumName: String,
        cover: String,
        year: Int,
        disks: DisksList,
    ) {
        val albumUploadId = UUID.randomUUID().toString()
        launch(
            context = SupervisorJob(),
            body = {
                state = state.copy(isLoaderVisible = true)
                val albumId = uploadAlbumMeta(cover = cover, artistId = artistId, albumName = albumName, year = year)

                disks.flatMapIndexed { diskNum, disk ->
                    disk.mapIndexed { trackPos, track ->
                        TrackDto(
                            title = track.title,
                            position = trackPos,
                            disk = diskNum.takeIf { disks.size > 1 },
                            albumId = albumId,
                        ) to track.path
                    }
                }
                    .map { (dto, path) ->
                        launch {
                            val uploadId = uploadRepository.initTrackUpload()
                            splitTrack(albumId = albumUploadId, uploadId = uploadId, path = path).map { chunks ->
                                launch {
                                    uploadRepository.uploadChunk(uploadId = uploadId, chunkPath = chunks)
                                }
                            }
                                .joinAll()

                            uploadRepository.finishUpload(uploadId = uploadId, meta = Json.encodeToString(dto))
                        }
                    }
                    .joinAll()

                _eventsFlow.emit(NewAlbumEvent.CloseDialog)
            },
            onFailure = {
                state = state.copy(isLoaderVisible = false, errorMsg = it.message)
            },
            finally = {
                removeUploadTempDir(albumId = albumUploadId)
            },
        )
    }

    private fun getCurrentAlbumDir(albumId: String): File {
        return File(
            System.getProperty("java.io.tmpdir"),
            "$APP_BASE_DIR${File.separatorChar}$albumId",
        )
    }

    private fun getCurrentUploadDir(albumId: String, uploadId: String): File {
        return File(
            getCurrentAlbumDir(albumId),
            uploadId,
        )
    }

    private fun removeUploadTempDir(albumId: String) {
        runCatching { getCurrentAlbumDir(albumId).deleteRecursively() }
    }

    private suspend fun uploadAlbumMeta(
        cover: String,
        artistId: String,
        albumName: String,
        year: Int,
    ): String {
        val coverUrl = uploadRepository.uploadImage(cover)

        return albumsRepository.createAlbum(
            artist = artistId,
            title = albumName,
            cover = coverUrl,
            year = year,
        )
    }

    private suspend fun splitTrack(albumId: String, uploadId: String, path: String): List<String> {
        val chunks = mutableListOf<String>()
        val fileName = path.substringAfterLast(File.separatorChar).substringBeforeLast(".")

        val chunksDir = getCurrentUploadDir(albumId, uploadId)

        if (!chunksDir.mkdirs()) {
            error("Unable to create chunks directory")
        }

        splitTrackNew(path).forEachIndexed { index, bytes ->
            val f = File.createTempFile(
                "${fileName}_",
                "_$index",
                chunksDir,
            )
            f.outputStream().use { it.write(bytes) }
            chunks.add(f.path)
        }

        return chunks
    }

    private suspend fun splitTrackNew(path: String): List<ByteArray> {
        val result = mutableListOf<ByteArray>()

        val track = File(path)
        val bytes = track.length()

        val readChannel = track.inputStream().toByteReadChannel()

        var offset = 0

        do {
            val buffer = ByteArray((bytes - offset).toInt().coerceAtMost(MAX_CHUNKS_SIZE))
            readChannel.readFully(out = buffer)
            result.add(buffer)
            offset += buffer.size
        } while (offset < bytes)

        return result
    }

    companion object {
        private const val MAX_CHUNKS_SIZE = 2 * 1024 * 1024 // 2 Mb
        private const val APP_BASE_DIR = "pixelplayer"
    }
}

@Serializable
private data class TrackDto(
    @SerialName("title") val title: String,
    @SerialName("position") val position: Int,
    @SerialName("disk") val disk: Int?,
    @SerialName("album_id") val albumId: String,
)
