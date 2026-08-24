package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOperationResponse(
    @SerialName("operatingHours")
    val operatingHours: String?,
    @SerialName("closedDays")
    val closedDays: String?,
    @SerialName("usageFee")
    val usageFee: String?,
    @SerialName("parkingAvailable")
    val parkingAvailable: Boolean?,
    @SerialName("indoorOutdoorType")
    val indoorOutdoorType: String?,
    @SerialName("lodgingType")
    val lodgingType: String?,
)
