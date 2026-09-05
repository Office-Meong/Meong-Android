package com.office.meong.presentation.explore.detail.model

import androidx.compose.runtime.Immutable
import com.office.meong.data.place.model.PlaceWalkCourse

@Immutable
data class ExploreWalkCourseUiModel(
    val id: Long,
    val courseName: String,
    val distanceKm: Double,
    val distanceFromPlaceKm: Double,
    val latitude: Double,
    val longitude: Double,
)

fun PlaceWalkCourse.toUiModel() = ExploreWalkCourseUiModel(
    id = id,
    courseName = courseName,
    distanceKm = distanceKm,
    distanceFromPlaceKm = distanceFromPlaceKm,
    latitude = startLatitude,
    longitude = startLongitude,
)
