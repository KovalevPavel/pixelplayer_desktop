package kov_p.pixelplayer_desktop

import kov_p.pixelplayer_desktop.core_credentials.credentialsModule
import kov_p.pixelplayer_desktop.core_storage.prefsModule
import kov_p.pixelplayer_desktop.core_tags.tagsModule
import org.koin.core.KoinApplication

fun KoinApplication.initDi() {
    modules(
        listOf(
            prefsModule,
            credentialsModule,
            tagsModule,
        )
    )
}
