package kov_p.pixelplayer_desktop.api_main_flow.di

import kov_p.pixelplayer_desktop.api_main_flow.MainFlowViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

object MainViewModelScope

val mainViewModelScope = module {
    scope<MainViewModelScope> {
        scopedOf(::MainFlowViewModel)
    }
}
