package kov_p.pixelplayer_desktop.domain_main_flow.upload

interface UploadRepository {
    suspend fun uploadImage(path: String): String
    suspend fun uploadImage(bytes: ByteArray): String
    suspend fun initTrackUpload(): String
    suspend fun uploadChunk(uploadId: String, chunkPath: String)
    suspend fun finishUpload(uploadId: String, meta: String)
}
