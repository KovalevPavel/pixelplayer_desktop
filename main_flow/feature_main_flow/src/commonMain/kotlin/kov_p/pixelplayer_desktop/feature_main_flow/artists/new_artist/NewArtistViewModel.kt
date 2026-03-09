package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kov_p.pixelplayer_desktop.core_ui.launch
import kov_p.pixelplayer_desktop.domain_main_flow.UpdateInfoInteractor
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.upload.UploadRepository

internal class NewArtistViewModel(
    private val uploadRepository: UploadRepository,
    private val artistsRepository: ArtistsRepository,
    private val updateInfo: UpdateInfoInteractor,
) : ViewModel() {
    var state: NewArtistState by mutableStateOf(NewArtistState.init)
        private set

    val eventsFlow: Flow<NewArtistEvent> by lazy { _eventsFlow }
    private val _eventsFlow = MutableSharedFlow<NewArtistEvent>()

    fun handleAction(action: NewArtistAction) {
        when (action) {
            is NewArtistAction.CreateArtist -> createNewArtist(name = action.name, avatar = action.avatar)
        }
    }

    private fun createNewArtist(
        name: String,
        avatar: String,
    ) {
        launch(
            body = {
                state = state.copy(isLoaderVisible = true, errorMsg = null)

                val avatarUrl = uploadRepository.uploadImage(avatar)
                artistsRepository.createNewArtist(name = name, avatarUrl = avatarUrl)
                updateInfo()
                _eventsFlow.emit(NewArtistEvent.CloseDialog)
            },
            onFailure = {
                state = state.copy(isLoaderVisible = false, errorMsg = it.message)
            },
        )
    }
}
