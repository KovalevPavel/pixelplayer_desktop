package kov_p.pixelplayer_desktop.feature_login.endpoint.di

import kov_p.pixelplayer_desktop.feature_login.endpoint.EndpointInputViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val endpointInputModule = module {
    viewModelOf(::EndpointInputViewModel)
}
