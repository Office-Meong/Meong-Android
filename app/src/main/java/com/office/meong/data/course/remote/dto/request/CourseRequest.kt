package com.office.meong.data.course.remote.dto.request

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseRequest(
    @SerialName("region")
    val region: Region,
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
    @SerialName("dogId")
    val dogId: Long,
    @SerialName("name")
    val name: String,
)