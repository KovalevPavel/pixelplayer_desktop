package kov_p.pixelplayer_desktop.core_credentials

import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.api_storage.SharedPreferences

class CredentialsRepositoryImpl(
    private val pref: SharedPreferences,
) : CredentialsRepository {
    override suspend fun logout() {
        saveNewToken(null)
    }

    override suspend fun getSavedEndpoint(): String? {
        return pref.getString(ENDPOINT_KEY)
    }

    override suspend fun getSavedToken(): String? {
        return pref.getString(TOKEN_KEY)
    }

    override suspend fun saveNewEndpoint(newEndpoint: String?) {
        pref.putString(key = ENDPOINT_KEY, value = newEndpoint)
    }

    override suspend fun saveNewToken(newToken: String?) {
        pref.putString(key = TOKEN_KEY, value = newToken)
    }

    companion object {
        private const val ENDPOINT_KEY = "endpoint"
        private const val TOKEN_KEY = "token"
    }
}
