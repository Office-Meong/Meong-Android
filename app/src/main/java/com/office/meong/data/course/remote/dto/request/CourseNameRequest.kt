package com.office.meong.data.course.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseNameRequest(
    @SerialName("name")
    val name: String,
)