package com.office.meong.data.course.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse

data class CourseSummary(
    val id: Long,
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val averageGrade: String,
    val workPlaceCount: Int,
    val foodCount: Int,
    val tourWalkCount: Int,
    val otherCount: Int,
    val totalPlaceCount: Int,
)

fun CourseSummaryResponse.toModel(): CourseSummary = CourseSummary(
    id = id,
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    totalDays = totalDays,
    averageGrade = averageGrade,
    workPlaceCount = workPlaceCount,
    foodCount = foodCount,
    tourWalkCount = tourWalkCount,
    otherCount = otherCount,
    totalPlaceCount = totalPlaceCount,
)
