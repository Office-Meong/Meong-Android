package com.office.meong.data.course.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseItemUpdateRequest(
    @SerialName("startTime")
    val startTime: String? = null,
    @SerialName("endTime")
    val endTime: String? = null,
    @SerialName("newPlaceId")
    val newPlaceId: Long? = null,
)