package com.office.meong.data.place.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceWalkCourseResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("courseName")
    val courseName: String = "",
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("distanceKm")
    val distanceKm: Double = 0.0,
    @SerialName("startLatitude")
    val startLatitude: Double = 0.0,
    @SerialName("startLongitude")
    val startLongitude: Double = 0.0,
    @SerialName("distanceFromPlaceKm")
    val distanceFromPlaceKm: Double = 0.0,
)
