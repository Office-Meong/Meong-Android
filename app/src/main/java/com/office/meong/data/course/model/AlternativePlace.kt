package com.office.meong.data.course.model

import com.office.meong.data.course.remote.dto.response.AlternativePlaceResponse

data class AlternativePlace(
    val placeId: Long,
    val placeName: String,
    val placeType: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val totalScore: Int,
    val grade: String,
    val cageNotRequired: Boolean,
)

fun AlternativePlaceResponse.toModel(): AlternativePlace = AlternativePlace(
    placeId = placeId,
    placeName = placeName,
    placeType = placeType,
    address = address,
    latitude = latitude,
    longitude = longitude,
    totalScore = totalScore,
    grade = grade,
    cageNotRequired = cageNotRequired,
)