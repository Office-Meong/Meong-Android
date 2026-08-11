package com.office.meong.data.course.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.course.remote.api.CourseService
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse
import javax.inject.Inject

class CourseDataSource @Inject constructor(
    private val courseService: CourseService,
) {
    suspend fun getCourses(): List<CourseSummaryResponse> =
        courseService.getCourses().getOrThrow()
}
