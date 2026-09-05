package com.office.meong.data.walkcourse.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WalkCourseResponse(
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
    @SerialName("distanceFromUserKm")
    val distanceFromUserKm: Double = 0.0
)
