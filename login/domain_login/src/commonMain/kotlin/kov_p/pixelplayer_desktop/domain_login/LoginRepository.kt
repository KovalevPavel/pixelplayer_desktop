package kov_p.pixelplayer_desktop.domain_login

interface LoginRepository {

    suspend fun checkEndpoint(endpoint: String): Boolean

    /**
     * @return token [String]
     */
    suspend fun loginUser(login: String, password: String): String
}
