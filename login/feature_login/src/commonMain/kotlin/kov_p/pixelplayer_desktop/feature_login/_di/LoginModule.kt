package kov_p.pixelplayer_desktop.feature_login._di

import androidx.compose.runtime.staticCompositionLocalOf
import kov_p.core_network.bindUnauthorizedHttpClient
import kov_p.pixelplayer_desktop.domain_login.LoginRepository
import kov_p.pixelplayer_desktop.feature_login._data.LoginRepositoryImpl
import kov_p.pixelplayer_desktop.feature_login.creds.CredentialsInputViewModel
import kov_p.pixelplayer_desktop.feature_login.endpoint.EndpointInputViewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module

object LoginScope

val loginModule = module {
    scope<LoginScope> {
        bindUnauthorizedHttpClient()
        scopedOf(::LoginRepositoryImpl).bind<LoginRepository>()
        scopedOf(::EndpointInputViewModel)
        scopedOf(::CredentialsInputViewModel)
    }
}

val LocalLoginScope = staticCompositionLocalOf<Scope> { error("No login scope found") }
