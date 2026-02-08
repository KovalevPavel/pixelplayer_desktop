package kov_p.pixelplayer_desktop.feature_main_flow.albums.new_album.ui

sealed interface AlbumCover {
    data class Binary(val bytes: ByteArray) : AlbumCover {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Binary

            return bytes.contentEquals(other.bytes)
        }

        override fun hashCode(): Int {
            return bytes.contentHashCode()
        }
    }

    data class FileSystem(val path: String) : AlbumCover
}
