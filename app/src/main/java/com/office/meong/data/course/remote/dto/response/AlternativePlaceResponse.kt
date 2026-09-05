package com.office.meong.data.course.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlternativePlaceResponse(
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String = "",
    @SerialName("placeType")
    val placeType: String = "",
    @SerialName("address")
    val address: String = "",
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("totalScore")
    val totalScore: Int = 0,
    @SerialName("grade")
    val grade: String = "",
    @SerialName("cageNotRequired")
    val cageNotRequired: Boolean = false,
)
