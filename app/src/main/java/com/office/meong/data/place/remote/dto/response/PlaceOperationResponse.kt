package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOperationResponse(
    @SerialName("operatingHours")
    val operatingHours: String? = null,
    @SerialName("closedDays")
    val closedDays: String? = null,
    @SerialName("usageFee")
    val usageFee: String? = null,
    @SerialName("parkingAvailable")
    val parkingAvailable: Boolean? = null,
    @SerialName("indoorOutdoorType")
    val indoorOutdoorType: String? = null,
    @SerialName("lodgingType")
    val lodgingType: String? = null,
)
