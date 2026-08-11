package com.office.meong.data.course.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.course.remote.dto.request.CourseRequest

data class CourseCreateInput(
    val region: Region,
    val startDate: String,
    val endDate: String,
    val workStartTime: String,
    val workEndTime: String,
    val workFocusLevel: String,
    val dogId: Long,
    val name: String,
)

fun CourseCreateInput.toDto() = CourseRequest(
    region = region,
    startDate = startDate,
    endDate = endDate,
    workStartTime = workStartTime,
    workEndTime = workEndTime,
    workFocusLevel = workFocusLevel,
    dogId = dogId,
    name = name,
)