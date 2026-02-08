package kov_p.pixelplayer_desktop.api_tags

data class TrackMeta(
    val title: String?,
    val albumTitle: String?,
    val artist: String?,
    val position: Int?,
    val trackTotal: Int?,
    val disk: Int?,
    val discTotal: Int?,
    val cover: ByteArray?,
    val year: Int?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrackMeta

        if (position != other.position) return false
        if (trackTotal != other.trackTotal) return false
        if (disk != other.disk) return false
        if (discTotal != other.discTotal) return false
        if (year != other.year) return false
        if (title != other.title) return false
        if (albumTitle != other.albumTitle) return false
        if (artist != other.artist) return false
        if (!cover.contentEquals(other.cover)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position ?: 0
        result = 31 * result + (trackTotal ?: 0)
        result = 31 * result + (disk ?: 0)
        result = 31 * result + (discTotal ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (albumTitle?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + (cover?.contentHashCode() ?: 0)
        return result
    }
}
