package kov_p.pixelplayer_desktop.feature_main_flow.artists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.UpdateInfoInteractor
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository

class ArtistsViewModel(
    private val artistsRepository: ArtistsRepository,
    private val updateInfo: UpdateInfoInteractor,
) : ViewModel() {
    var state: ArtistsState by mutableStateOf(ArtistsState.Loading)
        private set

    val eventsFlow: Flow<ArtistsEvent> by lazy { _eventsFlow }
    private val _eventsFlow = MutableSharedFlow<ArtistsEvent>()

    init {
        subscribeToArtistsList()
    }

    fun handleAction(action: ArtistsAction) {
        when (action) {
            is ArtistsAction.DeleteArtist -> {
                deleteArtist(artistId = action.artistId)
            }
        }
    }

    private fun deleteArtist(artistId: String) {
        launch(
            body = {
                artistsRepository.deleteArtist(artistId = artistId)
                updateInfo()
            },
            onFailure = {
                ArtistsEvent.ShowError(message = it.message.orEmpty()).let(::emitNewEvent)
            },
        )
    }

    private fun emitNewEvent(newEvent: ArtistsEvent) {
        viewModelScope.launch { _eventsFlow.emit(newEvent) }
    }

    private fun subscribeToArtistsList() {
        artistsRepository.artists
            .map { list -> list?.let { ArtistsState.Data(it) } }
            .onEach { newState ->
                newState?.let { state = it } ?: run { artistsRepository.getAllArtists() }
            }
            .launchIn(viewModelScope)
    }
}
