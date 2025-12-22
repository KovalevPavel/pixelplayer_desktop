package kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist.di

import kov_p.pixelplayer_desktop.feature_main_flow.artists.new_artist.NewArtistViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

object NewArtistScope

val newArtistModule = module {
    scope<NewArtistScope> {
        scopedOf(::NewArtistViewModel)
    }
}
