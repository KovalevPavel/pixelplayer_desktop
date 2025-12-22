package kov_p.pixelplayer_desktop.api_storage

import kotlinx.coroutines.flow.StateFlow

interface SharedPreferences {
    suspend fun getString(key: String): String?
    suspend fun putString(key: String, value: String?)

    suspend fun getDataFlow(key: String): StateFlow<String?>
}
