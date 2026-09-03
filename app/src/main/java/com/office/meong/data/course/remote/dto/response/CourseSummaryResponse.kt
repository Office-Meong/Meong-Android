package com.office.meong.data.course.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseSummaryResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String = "",
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("startDate")
    val startDate: String = "",
    @SerialName("endDate")
    val endDate: String = "",
    @SerialName("totalDays")
    val totalDays: Int = 0,
    @SerialName("averageGrade")
    val averageGrade: String = "",
    @SerialName("workPlaceCount")
    val workPlaceCount: Int = 0,
    @SerialName("foodCount")
    val foodCount: Int = 0,
    @SerialName("tourWalkCount")
    val tourWalkCount: Int = 0,
    @SerialName("otherCount")
    val otherCount: Int = 0,
    @SerialName("totalPlaceCount")
    val totalPlaceCount: Int = 0,
)
