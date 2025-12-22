package kov_p.pixelplayer_desktop.feature_login.creds.di

import kov_p.pixelplayer_desktop.feature_login.creds.CredentialsInputViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val credentialsInputModule = module {
    viewModelOf(::CredentialsInputViewModel)
}
