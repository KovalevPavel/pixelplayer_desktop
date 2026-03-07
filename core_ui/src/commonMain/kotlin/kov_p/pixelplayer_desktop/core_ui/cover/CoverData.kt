package kov_p.pixelplayer_desktop.core_ui.cover

sealed interface CoverData {
    data class Binary(val bytes: ByteArray) : CoverData {
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

    data class FileSystem(val path: String) : CoverData
}
