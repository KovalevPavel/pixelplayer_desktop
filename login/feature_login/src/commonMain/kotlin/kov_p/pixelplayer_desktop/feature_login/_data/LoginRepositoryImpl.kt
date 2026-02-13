package kov_p.pixelplayer_desktop.feature_login._data

import io.ktor.client.HttpClient
import kov_p.core_network.get
import kov_p.core_network.post
import kov_p.pixelplayer_desktop.api_credentials.CredentialsRepository
import kov_p.pixelplayer_desktop.domain_login.LoginRepository

class LoginRepositoryImpl(
    private val client: HttpClient,
    private val repository: CredentialsRepository,
) : LoginRepository {

    override suspend fun checkEndpoint(endpoint: String): Boolean {
        return client.get<EndpointValidationDto>(url = endpoint, path = "validate").validate == "ok"
    }

    override suspend fun loginUser(login: String, password: String): String {
        return client.post<String>(
            url = repository.getSavedEndpoint() ?: error("No saved endpoint"),
            path = "login",
            parameters = mapOf(
                "login" to login,
                "password" to password,
            ),
        )
    }
}
