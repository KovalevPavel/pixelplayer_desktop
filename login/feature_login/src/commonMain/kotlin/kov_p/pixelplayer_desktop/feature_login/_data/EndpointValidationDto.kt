package kov_p.pixelplayer_desktop.feature_login._data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EndpointValidationDto(
    @SerialName("validation")
    val validate: String? = null,
)
