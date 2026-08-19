package com.office.meong.data.place.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.place.remote.dto.response.PlaceWalkCourseResponse

data class PlaceWalkCourse(
    val id: Long,
    val courseName: String,
    val region: Region,
    val distanceKm: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val distanceFromPlaceKm: Double,
)

fun PlaceWalkCourseResponse.toModel(): PlaceWalkCourse = PlaceWalkCourse(
    id = id,
    courseName = courseName,
    region = region,
    distanceKm = distanceKm,
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    distanceFromPlaceKm = distanceFromPlaceKm,
)
