package com.office.meong.data.course.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseItemResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("dayNumber")
    val dayNumber: Int,
    @SerialName("visitOrder")
    val visitOrder: Int,
    @SerialName("slotLabel")
    val slotLabel: String,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("address")
    val address: String?,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("distanceFromPrevKm")
    val distanceFromPrevKm: Double?,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerialName("lodgingType")
    val lodgingType: String? = null,
)