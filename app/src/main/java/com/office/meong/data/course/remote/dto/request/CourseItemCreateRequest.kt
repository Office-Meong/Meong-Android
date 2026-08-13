package com.office.meong.data.course.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseItemCreateRequest(
    @SerialName("dayNumber")
    val dayNumber: Int,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("visitOrder")
    val visitOrder: Int? = null,
    @SerialName("startTime")
    val startTime: String? = null,
    @SerialName("endTime")
    val endTime: String? = null,
    @SerialName("slotLabel")
    val slotLabel: String? = null,
)
