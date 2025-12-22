package kov_p.core_network

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.appendPathSegments

suspend inline fun <reified T : Any> HttpClient.get(
    path: String = "",
    params: Map<String, String> = emptyMap(),
): T {
    val response = this.get {
        url {
            appendPathSegments("api", path)
            params.forEach { (k, v) -> parameter(k, v) }
        }
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.get(
    url: String,
    path: String = "",
    params: Map<String, String> = emptyMap(),
): T {
    val response = this.get {
        url(url)

        url {
            appendPathSegments("api", path)
            params.forEach { (k, v) -> parameter(k, v) }
        }
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.post(
    path: String = "",
    parameters: Map<String, String> = emptyMap(),
): T {
    val response = this.post {
        url {
            appendPathSegments("api", path)
            parameters.forEach { (k, v) -> parameter(k, v) }
        }
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.upload(
    path: String,
    crossinline form: FormBuilder.() -> Unit
): T {
    val response = this.submitFormWithBinaryData(
        formData = formData { form() },
    ) {
        url { appendPathSegments("api", path) }
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.post(
    path: String = "",
    parameters: Map<String, String> = emptyMap(),
    payload: String,
): T {
    val response = this.post {
        url {
            appendPathSegments("api", path)
            parameters.forEach { (k, v) -> parameter(k, v) }
        }

        setBody(payload)
    }

    return mapResponse<T>(response = response)
}
