package com.office.meong.data.course.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.model.region.Region
import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.course.model.CourseCreateInput
import com.office.meong.data.course.model.CourseSummary
import com.office.meong.data.course.remote.api.CourseService
import com.office.meong.data.course.remote.datasource.CourseDataSource
import com.office.meong.data.course.remote.dto.request.CourseItemCreateRequest
import com.office.meong.data.course.remote.dto.request.CourseItemReorderRequest
import com.office.meong.data.course.remote.dto.request.CourseItemUpdateRequest
import com.office.meong.data.course.remote.dto.request.CourseNameRequest
import com.office.meong.data.course.remote.dto.request.CourseRequest
import com.office.meong.data.course.remote.dto.response.CourseResponse
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeCourseService : CourseService {
    var getCoursesCallCount = 0
        private set

    private val summaryResponse = CourseSummaryResponse(
        id = 1,
        name = "강릉 워케이션",
        region = Region.GANGNEUNG,
        startDate = "2026-08-01",
        endDate = "2026-08-03",
        totalDays = 3,
        averageGrade = "A",
        workPlaceCount = 2,
        foodCount = 3,
        tourWalkCount = 1,
        otherCount = 0,
        totalPlaceCount = 6,
    )

    override suspend fun getCourses(): BaseResponse<List<CourseSummaryResponse>> {
        getCoursesCallCount++
        return BaseResponse(success = true, data = listOf(summaryResponse))
    }

    override suspend fun getDetailCourse(courseId: Long) = throw NotImplementedError()

    override suspend fun postCourse(courseRequest: CourseRequest): BaseResponse<CourseResponse> =
        BaseResponse(
            success = true,
            data = CourseResponse(
                id = 2,
                name = courseRequest.name,
                region = courseRequest.region,
                startDate = courseRequest.startDate,
                endDate = courseRequest.endDate,
                workStartTime = courseRequest.workStartTime,
                workEndTime = courseRequest.workEndTime,
                workFocusLevel = courseRequest.workFocusLevel,
                totalDays = 3,
                dayItems = emptyMap(),
                createdAt = "2026-08-11T00:00:00.000Z",
            )
        )

    override suspend fun deleteCourse(courseId: Long) = throw NotImplementedError()
    override suspend fun postCourseItem(
        courseId: Long,
        courseItemCreateRequest: CourseItemCreateRequest
    ) = throw NotImplementedError()

    override suspend fun putCourseItem(
        courseId: Long,
        itemId: Long,
        courseItemUpdateRequest: CourseItemUpdateRequest
    ) = throw NotImplementedError()

    override suspend fun getCourseItemAlternatives(courseId: Long, itemId: Long) = throw NotImplementedError()
    override suspend fun deleteCourseItem(
        courseId: Long,
        itemId: Long
    ): BaseResponse<CourseResponse> = throw NotImplementedError()

    override suspend fun patchCourseName(courseId: Long, courseNameRequest: CourseNameRequest) =
        throw NotImplementedError()

    override suspend fun patchCourseItemsReorder(
        courseId: Long,
        courseItemReorderRequest: CourseItemReorderRequest
    ) = throw NotImplementedError()
}

class CourseRepositoryImplCacheTest {

    private fun buildRepository(fakeService: FakeCourseService) = CourseRepositoryImpl(
        courseDataSource = CourseDataSource(fakeService),
        coursesCache = InMemoryCache(),
    )

    @Test
    fun `Home과 MyCourse가 각각 getCourses를 호출해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeCourseService()
        val repository = buildRepository(fakeService)

        val homeResult = repository.getCourses()
        val myCourseResult = repository.getCourses()

        assertEquals(1, fakeService.getCoursesCallCount)
        assertEquals(homeResult.getOrNull(), myCourseResult.getOrNull())
    }

    @Test
    fun `코스를 생성하면 캐시가 무효화되어 다음 조회는 다시 네트워크를 탄다`() = runBlocking {
        val fakeService = FakeCourseService()
        val repository = buildRepository(fakeService)

        repository.getCourses()
        repository.createCourse(
            CourseCreateInput(
                region = Region.GANGNEUNG,
                startDate = "2026-08-01",
                endDate = "2026-08-03",
                workStartTime = "09:00",
                workEndTime = "18:00",
                workFocusLevel = "MEDIUM",
                dogId = 1,
                name = "새 코스",
            )
        )
        repository.getCourses()

        assertEquals(2, fakeService.getCoursesCallCount)
    }

    @Test
    fun `동시에 두 화면이 처음 조회해도 네트워크는 한 번만 나간다`() = runBlocking {
        val fakeService = FakeCourseService()
        val repository = buildRepository(fakeService)

        val homeDeferred = async { repository.getCourses() }
        val myCourseDeferred = async { repository.getCourses() }
        val results = listOf(homeDeferred.await(), myCourseDeferred.await())

        assertEquals(1, fakeService.getCoursesCallCount)
        assertEquals(results[0].getOrNull(), results[1].getOrNull())
    }
}
