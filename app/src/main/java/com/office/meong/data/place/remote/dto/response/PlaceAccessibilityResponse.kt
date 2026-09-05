package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceAccessibilityResponse(
    @SerialName("hasParking")
    val hasParking: Boolean = false,
    @SerialName("strollerAccessible")
    val strollerAccessible: Boolean = false,
    @SerialName("hasRamp")
    val hasRamp: Boolean = false,
    @SerialName("dataAvailable")
    val dataAvailable: Boolean = false,
)
