package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceAccessibilityResponse(
    @SerialName("hasParking")
    val hasParking: Boolean,
    @SerialName("strollerAccessible")
    val strollerAccessible: Boolean,
    @SerialName("hasRamp")
    val hasRamp: Boolean,
    @SerialName("dataAvailable")
    val dataAvailable: Boolean,
)
