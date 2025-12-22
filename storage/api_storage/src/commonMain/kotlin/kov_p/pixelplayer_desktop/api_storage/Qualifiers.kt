package kov_p.pixelplayer_desktop.api_storage

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

val authPreferences = object : Qualifier {
    override val value: QualifierValue = "authPreferences"
}
