package kov_p.core_network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@PublishedApi
internal suspend inline fun <reified T : Any> mapResponse(
    response: HttpResponse,
): T {
    return when (response.status.value) {
        in 200..299 -> {
            response.body<T>()
        }

        else -> {
            val jsonString = response.bodyAsText()
            val error = Json.decodeFromString<ErrorWrapper>(jsonString)
            error(error.message.orEmpty())
        }
    }
}

@PublishedApi
@Serializable
internal class ErrorWrapper(
    @SerialName("error")
    val message: String?,
)
