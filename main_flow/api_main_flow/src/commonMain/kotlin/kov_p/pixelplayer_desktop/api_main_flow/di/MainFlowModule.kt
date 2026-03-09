package kov_p.pixelplayer_desktop.api_main_flow.di

import kov_p.core_network.bindAuthorizedHttpClient
import kov_p.pixelplayer_desktop.domain_main_flow.UpdateInfoInteractor
import kov_p.pixelplayer_desktop.domain_main_flow.albums.AlbumsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.artists.ArtistsRepository
import kov_p.pixelplayer_desktop.domain_main_flow.upload.UploadRepository
import kov_p.pixelplayer_desktop.feature_main_flow._data.UpdateInfoInteractorImpl
import kov_p.pixelplayer_desktop.feature_main_flow._data.UploadRepositoryImpl
import kov_p.pixelplayer_desktop.feature_main_flow.albums.AlbumsViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.albums.data.AlbumsRepositoryImpl
import kov_p.pixelplayer_desktop.feature_main_flow.artists.ArtistsViewModel
import kov_p.pixelplayer_desktop.feature_main_flow.artists.data.ArtistsRepositoryImpl
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.bind
import org.koin.dsl.module

object MainFlowScope

fun mainFlowModule(baseUrl: String, token: String) = module {
    scope<MainFlowScope> {
        bindAuthorizedHttpClient(baseUrl = baseUrl, token = token)
        scopedOf(::ArtistsRepositoryImpl).bind<ArtistsRepository>()
        scopedOf(::ArtistsViewModel)

        scopedOf(::AlbumsRepositoryImpl).bind<AlbumsRepository>()
        scopedOf(::AlbumsViewModel)
        scopedOf(::UploadRepositoryImpl).bind<UploadRepository>()

        scopedOf(::UpdateInfoInteractorImpl).bind<UpdateInfoInteractor>()
    }
}
