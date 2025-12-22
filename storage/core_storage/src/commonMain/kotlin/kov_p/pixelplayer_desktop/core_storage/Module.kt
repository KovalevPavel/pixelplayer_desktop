package kov_p.pixelplayer_desktop.core_storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kov_p.pixelplayer_desktop.api_storage.SharedPreferences
import kov_p.pixelplayer_desktop.api_storage.authPreferences
import okio.Path.Companion.toPath
import org.koin.dsl.module
import java.io.File

val prefsModule = module {
    single<SharedPreferences>(qualifier = authPreferences) {
        SharedPreferences(
            prefs = createPreferences(PrefType.AuthPreferences)
        )
    }
}

private fun createPreferences(type: PrefType): DataStore<Preferences> {
    val file = File(System.getProperty("java.io.tmpdir"), type.filename)

    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { file.absolutePath.toPath() }
    )
}
