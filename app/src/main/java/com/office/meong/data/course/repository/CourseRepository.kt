package com.office.meong.data.course.repository

import com.office.meong.data.course.model.AlternativePlace
import com.office.meong.data.course.model.CourseCreateInput
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.data.course.model.CourseSummary

interface CourseRepository {
    suspend fun getCourses(): Result<List<CourseSummary>>
    suspend fun getDetailCourse(courseId: Long): Result<CourseDetail>
    suspend fun createCourse(request: CourseCreateInput): Result<CourseDetail>
    suspend fun deleteCourse(courseId: Long): Result<Unit>
    suspend fun addCourseItem(
        courseId: Long,
        dayNumber: Int,
        placeId: Long,
        visitOrder: Int? = null,
        startTime: String? = null,
        endTime: String? = null,
        slotLabel: String? = null
    ): Result<CourseDetail>
    suspend fun updateCourseItem(
        courseId: Long,
        itemId: Long,
        startTime: String?,
        endTime: String?,
        newPlaceId: Long?
    ): Result<CourseDetail>
    suspend fun getCourseItemAlternatives(courseId: Long, itemId: Long): Result<List<AlternativePlace>>
    suspend fun updateCourseName(courseId: Long, name: String): Result<CourseDetail>
    suspend fun reorderCourseItems(courseId: Long, dayNumber: Int, itemIds: List<Long>): Result<CourseDetail>
}
