package kov_p.pixelplayer_desktop.feature_login.init.di

import kov_p.pixelplayer_desktop.feature_login.init.InitViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val initModule = module {
    viewModelOf(::InitViewModel)
}
