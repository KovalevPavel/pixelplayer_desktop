package kov_p.pixelplayer_desktop.feature_main_flow.artists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository

class ArtistsViewModel(
    private val artistsRepository: ArtistsRepository,
) : ViewModel() {
    var state: ArtistsState by mutableStateOf(ArtistsState.Loading)
        private set

    init {
        ArtistsAction.FetchData.let(::handleAction)
    }

    fun handleAction(action: ArtistsAction) {
        when (action) {
            ArtistsAction.FetchData -> {
                fetchData()
            }

            is ArtistsAction.DeleteArtist -> {
                deleteArtist(artistId = action.artistId)
            }
        }
    }

    private fun fetchData() {
        launch(
            body = {
                state = artistsRepository.getAllArtists().let(ArtistsState::Data)
            },
            onFailure = {
                print("error while artists:\n$it")
            },
        )
    }

    private fun deleteArtist(artistId: String) {
        launch(
            body = {
                artistsRepository.deleteArtist(artistId = artistId)
                ArtistsAction.FetchData.let(::handleAction)
            },
            onFailure = {
                println("error while delete:\n$it")
            },
        )
    }
}
