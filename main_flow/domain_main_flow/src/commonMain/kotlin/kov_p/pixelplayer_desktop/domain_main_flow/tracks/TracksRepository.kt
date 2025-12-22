package kov_p.pixelplayer_desktop.domain_main_flow.tracks

interface TracksRepository {
    suspend fun getAllTracks(): List<TrackVo>
}
