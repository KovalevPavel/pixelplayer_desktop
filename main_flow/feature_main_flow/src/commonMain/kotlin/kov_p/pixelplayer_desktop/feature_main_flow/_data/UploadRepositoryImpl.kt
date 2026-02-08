package kov_p.pixelplayer_desktop.feature_main_flow._data

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.appendPathSegments
import io.ktor.http.headers
import kov_p.core_network.post
import kov_p.core_network.upload
import kov_p.pixelplayer_desktop.domain_main_flow.upload.UploadRepository
import java.io.File

class UploadRepositoryImpl(
    private val client: HttpClient,
) : UploadRepository {
    override suspend fun uploadImage(path: String): String {
        val bytes = File(path).readBytes()
        return uploadImage(bytes = bytes)
    }

    override suspend fun uploadImage(bytes: ByteArray): String {
        return client.upload<String>(
            path = "upload/img",
        ) {
            append(
                key = "img",
                value = bytes,
                headers = headers {
                    this.append(
                        HttpHeaders.ContentDisposition, "filename=\"img\""
                    )
                },
            )
        }
    }

    override suspend fun initTrackUpload(): String {
        return client.post<String>(path = "upload/music")
    }

    override suspend fun uploadChunk(uploadId: String, chunkPath: String) {
        val file = File(chunkPath)
        val chunkId = chunkPath.substringAfterLast("_").toInt()

        client.upload<String>(
            path = "upload/chunk/$uploadId/$chunkId",
        ) {
            append(
                key = "file",
                value = file.readBytes(),
                headers = headers {
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                },
            )
        }
    }

    override suspend fun finishUpload(uploadId: String, meta: String) {
        client.post {
            url {
                appendPathSegments("api/upload/finish/$uploadId")
            }

            headers {
                append(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json,
                )
            }

            setBody(meta)
        }
    }
}
