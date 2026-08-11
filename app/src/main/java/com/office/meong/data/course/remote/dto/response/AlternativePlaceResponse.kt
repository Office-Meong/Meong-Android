package com.office.meong.data.course.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlternativePlaceResponse(
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("address")
    val address: String,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("totalScore")
    val totalScore: Int,
    @SerialName("grade")
    val grade: String,
    @SerialName("cageNotRequired")
    val cageNotRequired: Boolean,
)