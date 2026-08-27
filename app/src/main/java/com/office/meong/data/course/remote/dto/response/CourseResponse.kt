package com.office.meong.data.course.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("endDate")
    val endDate: String,
    @SerialName("workStartTime")
    val workStartTime: String,
    @SerialName("workEndTime")
    val workEndTime: String,
    @SerialName("workFocusLevel")
    val workFocusLevel: String,
    @SerialName("totalDays")
    val totalDays: Int,
    @SerialName("dayItems")
    val dayItems: Map<String, List<CourseItemResponse>>,
    @SerialName("dayReturnToAccommKm")
    val dayReturnToAccommKm: Map<String, Double> = emptyMap(),
    @SerialName("createdAt")
    val createdAt: String,
)