package com.office.meong.data.course.remote.datasource

import com.office.meong.core.network.model.ApiException
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.course.remote.api.CourseService
import com.office.meong.data.course.remote.dto.request.CourseItemUpdateRequest
import com.office.meong.data.course.remote.dto.request.CourseNameRequest
import com.office.meong.data.course.remote.dto.request.CourseRequest
import com.office.meong.data.course.remote.dto.response.AlternativePlaceResponse
import com.office.meong.data.course.remote.dto.response.CourseResponse
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse
import javax.inject.Inject

class CourseDataSource @Inject constructor(
    private val courseService: CourseService,
) {
    suspend fun getCourses(): List<CourseSummaryResponse> =
        courseService.getCourses().getOrThrow()

    suspend fun getDetailCourse(courseId: Long): CourseResponse =
        courseService.getDetailCourse(courseId).getOrThrow()

    suspend fun postCourse(courseRequest: CourseRequest): CourseResponse =
        courseService.postCourse(courseRequest).getOrThrow()

    suspend fun deleteCourse(courseId: Long) {
        val response = courseService.deleteCourse(courseId)
        if (!response.success) throw ApiException(response.message)
    }

    suspend fun putCourseItem(
        courseId: Long,
        itemId: Long,
        courseItemUpdateRequest: CourseItemUpdateRequest
    ): CourseResponse =
        courseService.putCourseItem(courseId, itemId, courseItemUpdateRequest).getOrThrow()

    suspend fun getCourseItemAlternatives(
        courseId: Long,
        itemId: Long
    ): List<AlternativePlaceResponse> =
        courseService.getCourseItemAlternatives(courseId, itemId).getOrThrow()

    suspend fun patchCourseName(courseId: Long, name: String): CourseResponse =
        courseService.patchCourseName(
            courseId = courseId,
            courseNameRequest = CourseNameRequest(name = name)
        ).getOrThrow()
}