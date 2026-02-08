package kov_p.pixelplayer_desktop.core_tags

import kov_p.pixelplayer_desktop.api_tags.TagsManager
import kov_p.pixelplayer_desktop.api_tags.TrackMeta
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.images.ArtworkFactory
import java.io.File

internal class TagsManagerImpl : TagsManager {
    override suspend fun getTrackMeta(file: String, readCover: Boolean): TrackMeta? {
        return AudioFileIO.read(File(file))
            .tag
            ?.let {
                TrackMeta(
                    title = it.getFirst(FieldKey.TITLE),
                    albumTitle = it.getFirst(FieldKey.ALBUM),
                    artist = it.getFirst(FieldKey.ALBUM_ARTIST),
                    position = it.getFirst(FieldKey.TRACK)?.toIntOrNull(),
                    trackTotal = it.getFirst(FieldKey.TRACK_TOTAL)?.toIntOrNull(),
                    disk = it.getFirst(FieldKey.DISC_NO).toIntOrNull(),
                    discTotal = it.getFirst(FieldKey.DISC_TOTAL).toIntOrNull(),
                    cover = it.firstArtwork?.binaryData,
                    year = run {
                        it.getFirst(FieldKey.ALBUM_YEAR).toIntOrNull()
                            ?: it.getFirst(FieldKey.ORIGINAL_YEAR).toIntOrNull()
                            ?: it.getFirst(FieldKey.YEAR).toIntOrNull()
                    },
                )
            }
    }

    override suspend fun writeTrackMeta(file: String, meta: TrackMeta) {
        val f = File(file)
        val audioFile = AudioFileIO.read(f)
        val cover = ArtworkFactory.createArtworkFromFile(null).apply {
            mimeType = "image/*"
            binaryData = meta.cover
            pictureType = 3
        }

        audioFile.createDefaultTag().apply {
            setField(FieldKey.TITLE, meta.title)
            setField(FieldKey.ALBUM, meta.albumTitle)
            setField(FieldKey.ALBUM_ARTIST, meta.artist)
            setField(FieldKey.TRACK, meta.position.toString())
            setField(FieldKey.TRACK_TOTAL, meta.trackTotal.toString())
            setField(FieldKey.DISC_NO, meta.disk.toString())
            setField(FieldKey.DISC_TOTAL, meta.discTotal.toString())
            setField(FieldKey.ALBUM_YEAR, meta.year.toString())
            setField(cover)
        }

        audioFile.commit()
    }
}
