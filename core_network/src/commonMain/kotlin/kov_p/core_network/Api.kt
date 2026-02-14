package kov_p.core_network

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
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
        buildApiUrl(path = path, params = params)
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.get(
    url: String,
    path: String = "",
    params: Map<String, String> = emptyMap(),
): T {
    val response = this.get {
        buildApiUrl(baseUrl = url, path = path, params = params)
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.post(
    url: String,
    path: String,
    parameters: Map<String, String>,
): T {
    val response = this.post {
        buildApiUrl(baseUrl = url, path = path, params = parameters)
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.post(
    path: String = "",
    parameters: Map<String, String> = emptyMap(),
): T {
    val response = this.post {
        buildApiUrl(path = path, params = parameters)
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
        buildApiUrl(path = path)
    }

    return mapResponse<T>(response = response)
}

suspend inline fun <reified T : Any> HttpClient.post(
    path: String = "",
    parameters: Map<String, String> = emptyMap(),
    payload: String,
): T {
    val response = this.post {
        buildApiUrl(path = path, params = parameters)

        setBody(payload)
    }

    return mapResponse<T>(response = response)
}

@PublishedApi
internal fun HttpRequestBuilder.buildApiUrl(
    baseUrl: String? = null,
    path: String = "",
    params: Map<String, String> = emptyMap(),
) {
    url {
        baseUrl?.takeUnless(String::isEmpty)?.let { url(it) }
        appendPathSegments("api", path)
        params.forEach { (key, value) -> parameter(key, value) }
    }
}
