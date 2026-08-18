package com.office.meong.data.course.repositoryimpl

import android.util.LruCache
import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.cache.getOrFetch
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
    private val alternativesCache = LruCache<String, List<AlternativePlace>>(ALTERNATIVES_CACHE_SIZE)

    // 코스 생성 응답이 이미 AI가 만든 완성된 CourseDetail을 담고 있어서, 생성 직후 결과 화면 진입 시
    // 같은 코스를 다시 GET하는 걸 한 번 건너뛰기 위한 1회용 캐시.
    private var justCreatedCourse: CourseDetail? = null

    override suspend fun getCourses(): Result<List<CourseSummary>> = suspendRunCatching {
        coursesCache.getOrFetch { courseDataSource.getCourses().map { it.toModel() } }
    }

    override suspend fun getDetailCourse(courseId: Long): Result<CourseDetail> = suspendRunCatching {
        justCreatedCourse?.takeIf { it.id == courseId }?.also { justCreatedCourse = null }
            ?: courseDataSource.getDetailCourse(courseId).toModel()
    }

    override suspend fun createCourse(request: CourseCreateInput): Result<CourseDetail> = suspendRunCatching {
        courseDataSource.postCourse(request.toDto()).toModel()
            .also {
                coursesCache.invalidate()
                justCreatedCourse = it
            }
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
        ).toModel().also {
            coursesCache.invalidate()
            alternativesCache.remove(alternativesCacheKey(courseId, itemId))
        }
    }

    override suspend fun getCourseItemAlternatives(
        courseId: Long,
        itemId: Long
    ): Result<List<AlternativePlace>> = suspendRunCatching {
        alternativesCache.getOrFetch(alternativesCacheKey(courseId, itemId)) {
            courseDataSource.getCourseItemAlternatives(courseId, itemId).map { it.toModel() }
        }
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

    companion object {
        private const val ALTERNATIVES_CACHE_SIZE = 20
        private fun alternativesCacheKey(courseId: Long, itemId: Long) = "$courseId:$itemId"
    }
}