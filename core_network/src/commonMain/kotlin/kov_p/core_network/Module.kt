package kov_p.core_network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ioDispatcher
import kotlinx.serialization.json.Json
import org.koin.dsl.ScopeDSL
import java.net.URI

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.defaultLogging() {
    install(Logging) {
        logger = Logger.SIMPLE
        level = LogLevel.HEADERS
        this.format = LoggingFormat.OkHttp
    }
}

private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.defaultNegotiations() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            },
        )
    }
}

fun ScopeDSL.bindUnauthorizedHttpClient() {
    scoped<HttpClient> {
        HttpClient(OkHttp) {
            engine {
                dispatcher = ioDispatcher()
            }
            defaultLogging()
            defaultNegotiations()
        }
    }
}

fun ScopeDSL.bindAuthorizedHttpClient(
    baseUrl: String,
    token: String,
) {
    scoped<HttpClient> {
        HttpClient(OkHttp) {
            defaultRequest {
                URI.create(baseUrl).also { uri -> url(scheme = uri.scheme, host = uri.host) }
                header("Authorization", token)
                contentType(ContentType.Application.Json)
            }
            engine {
                dispatcher = ioDispatcher()
            }
            defaultLogging()
            defaultNegotiations()
        }
    }
}
