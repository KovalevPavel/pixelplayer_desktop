package kov_p.pixelplayer_desktop.core_storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kov_p.pixelplayer_desktop.api_storage.SharedPreferences

internal class SharedPreferences(
    private val prefs: DataStore<Preferences>,
) : SharedPreferences {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getString(key: String): String? {
        val k = stringPreferencesKey(key)

        return prefs.data.firstOrNull()?.get(k)
    }

    override suspend fun putString(key: String, value: String?) {
        val k = stringPreferencesKey(key)
        prefs.updateData {
            it.toMutablePreferences().also { preferences ->
                value?.let { preferences[k] = value }
                    ?: run { preferences.remove(k) }
            }
        }
    }

    override suspend fun getDataFlow(key: String): StateFlow<String?> {
        val k = stringPreferencesKey(key)
        return prefs.data
            .map { it[k] }
            .stateIn(
                scope = scope,
                started = SharingStarted.Eagerly,
                initialValue = null,
            )
    }
}
