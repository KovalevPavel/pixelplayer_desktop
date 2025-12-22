package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.di

import kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.NewAlbumViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

object NewAlbumScope

val newAlbumModule = module {
    scope<NewAlbumScope> {
        scopedOf(::NewAlbumViewModel)
    }
}
