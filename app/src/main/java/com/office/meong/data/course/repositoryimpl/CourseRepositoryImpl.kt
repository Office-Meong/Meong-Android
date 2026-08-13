package com.office.meong.data.course.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.course.model.AlternativePlace
import com.office.meong.data.course.model.CourseCreateInput
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.data.course.model.CourseSummary
import com.office.meong.data.course.model.toDto
import com.office.meong.data.course.model.toModel
import com.office.meong.data.course.remote.datasource.CourseDataSource
import com.office.meong.data.course.remote.dto.request.CourseItemCreateRequest
import com.office.meong.data.course.remote.dto.request.CourseItemUpdateRequest
import com.office.meong.data.course.repository.CourseRepository
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val courseDataSource: CourseDataSource,
    private val coursesCache: InMemoryCache<List<CourseSummary>>,
) : CourseRepository {

    override suspend fun getCourses(): Result<List<CourseSummary>> = suspendRunCatching {
        coursesCache.getOrFetch { courseDataSource.getCourses().map { it.toModel() } }
    }

    override suspend fun getDetailCourse(courseId: Long): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.getDetailCourse(courseId).toModel()
    }

    override suspend fun createCourse(request: CourseCreateInput): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.postCourse(request.toDto()).toModel().also { coursesCache.invalidate() }
    }

    override suspend fun deleteCourse(courseId: Long): Result<Unit> = suspendRunCatching {
        courseDataSource.deleteCourse(courseId)
        coursesCache.invalidate()
    }

    override suspend fun addCourseItem(
        courseId: Long,
        dayNumber: Int,
        placeId: Long,
        visitOrder: Int?,
        startTime: String?,
        endTime: String?,
        slotLabel: String?
    ): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.postCourseItem(
            courseId = courseId,
            courseItemCreateRequest = CourseItemCreateRequest(
                dayNumber = dayNumber,
                placeId = placeId,
                visitOrder = visitOrder,
                startTime = startTime,
                endTime = endTime,
                slotLabel = slotLabel
            )
        ).toModel().also { coursesCache.invalidate() }
    }

    override suspend fun updateCourseItem(
        courseId: Long,
        itemId: Long,
        startTime: String?,
        endTime: String?,
        newPlaceId: Long?
    ): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.putCourseItem(
            courseId = courseId,
            itemId = itemId,
            courseItemUpdateRequest = CourseItemUpdateRequest(
                startTime = startTime,
                endTime = endTime,
                newPlaceId = newPlaceId
            )
        ).toModel().also { coursesCache.invalidate() }
    }

    override suspend fun getCourseItemAlternatives(
        courseId: Long,
        itemId: Long
    ): Result<List<AlternativePlace>> = suspendRunCatching {
        courseDataSource.getCourseItemAlternatives(courseId, itemId).map { it.toModel() }
    }

    override suspend fun updateCourseName(courseId: Long, name: String): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.patchCourseName(courseId, name).toModel().also { coursesCache.invalidate() }
    }

    override suspend fun reorderCourseItems(
        courseId: Long,
        dayNumber: Int,
        itemIds: List<Long>
    ): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.patchCourseItemsReorder(
            courseId = courseId,
            dayNumber = dayNumber,
            itemIds = itemIds
        ).toModel().also { coursesCache.invalidate() }
    }
}