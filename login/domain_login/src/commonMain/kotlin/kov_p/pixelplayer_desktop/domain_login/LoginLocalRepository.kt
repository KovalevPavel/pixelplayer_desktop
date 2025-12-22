package kov_p.pixelplayer_desktop.domain_login

interface LoginLocalRepository {
    suspend fun getSavedEndpoint(): String?
    suspend fun getSavedToken(): String?

    suspend fun saveNewEndpoint(newEndpoint: String)
    suspend fun saveNewToken(newToken: String)
}
