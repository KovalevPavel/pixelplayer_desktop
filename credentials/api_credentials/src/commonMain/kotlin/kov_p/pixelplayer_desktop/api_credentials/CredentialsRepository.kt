package kov_p.pixelplayer_desktop.api_credentials

interface CredentialsRepository {
    suspend fun getSavedEndpoint(): String?
    suspend fun getSavedToken(): String?

    suspend fun saveNewEndpoint(newEndpoint: String?)
    suspend fun saveNewToken(newToken: String?)

    suspend fun logout()
}
