package com.office.meong.data.course.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseItemReorderRequest(
    @SerialName("dayNumber")
    val dayNumber: Int,
    @SerialName("itemIds")
    val itemIds: List<Long>,
)
