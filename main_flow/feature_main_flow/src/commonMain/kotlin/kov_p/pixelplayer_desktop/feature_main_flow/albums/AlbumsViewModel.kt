package kov_p.pixelplayer_desktop.feature_main_flow.albums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.UpdateInfoInteractor
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository

class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val updateInfo: UpdateInfoInteractor,
) : ViewModel() {
    var state: AlbumsState by mutableStateOf(AlbumsState.Loading)
        private set

    init {
        subscribeToAlbums()
    }

    fun handleAction(action: AlbumAction) {
        when (action) {
            is AlbumAction.DeleteAlbum -> deleteAlbum(albumId = action.albumId)
        }
    }

    private fun deleteAlbum(albumId: String) {
        launch(
            body = {
                albumsRepository.deleteAlbum(albumId)
                updateInfo()
            },
        )
    }

    private fun subscribeToAlbums() {
        albumsRepository.albums
            .map { list -> list?.let(AlbumsState::Data) }
            .onEach { newState ->
                newState?.let { state = newState } ?: run { albumsRepository.getAllAlbums() }
            }
            .launchIn(viewModelScope)
    }
}
