package com.office.meong.data.course.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseSummaryResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: Region,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("totalDays")
    val totalDays: Int,
    @SerialName("averageGrade")
    val averageGrade: String,
    @SerialName("workPlaceCount")
    val workPlaceCount: Int,
    @SerialName("foodCount")
    val foodCount: Int,
    @SerialName("tourWalkCount")
    val tourWalkCount: Int,
    @SerialName("otherCount")
    val otherCount: Int,
    @SerialName("totalPlaceCount")
    val totalPlaceCount: Int,
)
