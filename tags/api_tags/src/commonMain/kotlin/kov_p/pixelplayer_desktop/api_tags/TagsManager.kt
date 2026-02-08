package kov_p.pixelplayer_desktop.api_tags

interface TagsManager {
    suspend fun getTrackMeta(file: String, readCover: Boolean): TrackMeta?
    suspend fun writeTrackMeta(file: String, meta: TrackMeta)
}
