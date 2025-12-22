package kov_p.pixelplayer_desktop.core_credentials

import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.api_storage.authPreferences
import org.koin.dsl.module

val credentialsModule = module {
    single<CredentialsRepository> {
        CredentialsRepositoryImpl(get(qualifier = authPreferences))
    }
}
