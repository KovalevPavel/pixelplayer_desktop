package kov_p.pixelplayer_desktop.feature_main_flow.albums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository

class AlbumsViewModel(
    private val repository: AlbumsRepository,
) : ViewModel() {
    var state: AlbumsState by mutableStateOf(AlbumsState.Loading)
        private set

    init {
        AlbumAction.FetchAlbums.let(::handleAction)
    }

    fun handleAction(action: AlbumAction) {
        when (action) {
            AlbumAction.FetchAlbums -> fetchAlbums()
            is AlbumAction.DeleteAlbum -> deleteAlbum(albumId = action.albumId)
        }
    }

    private fun fetchAlbums() {
        launch(
            body = {
                state = repository.getAllAlbums().let(AlbumsState::Data)
            }
        )
    }

    private fun deleteAlbum(albumId: String) {
        launch(
            body = {
                repository.deleteAlbum(albumId)
                AlbumAction.FetchAlbums.let(::handleAction)
            },
        )
    }
}
