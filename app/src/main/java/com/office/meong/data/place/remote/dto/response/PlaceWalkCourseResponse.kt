package com.office.meong.data.place.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceWalkCourseResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("courseName")
    val courseName: String,
    @SerialName("region")
    val region: Region,
    @SerialName("distanceKm")
    val distanceKm: Double,
    @SerialName("startLatitude")
    val startLatitude: Double,
    @SerialName("startLongitude")
    val startLongitude: Double,
    @SerialName("distanceFromPlaceKm")
    val distanceFromPlaceKm: Double,
)
