package com.office.meong.data.walkcourse.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.walkcourse.remote.dto.response.WalkCourseResponse

data class WalkCourse(
    val id: Long,
    val courseName: String,
    val region: Region,
    val distanceKm: Double,
    val startLatitude: Double,
    val startLongitude: Double,
    val distanceFromUserKm: Double,
)

fun WalkCourseResponse.toModel(): WalkCourse = WalkCourse(
    id = id,
    courseName = courseName,
    region = region,
    distanceKm = distanceKm,
    startLatitude = startLatitude,
    startLongitude = startLongitude,
    distanceFromUserKm = distanceFromUserKm,
)
