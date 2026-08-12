package com.office.meong.data.course.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.course.remote.dto.request.CourseItemReorderRequest
import com.office.meong.data.course.remote.dto.request.CourseItemUpdateRequest
import com.office.meong.data.course.remote.dto.request.CourseNameRequest
import com.office.meong.data.course.remote.dto.request.CourseRequest
import com.office.meong.data.course.remote.dto.response.AlternativePlaceResponse
import com.office.meong.data.course.remote.dto.response.CourseResponse
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CourseService {

    @GET("courses")
    suspend fun getCourses(): BaseResponse<List<CourseSummaryResponse>>

    @GET("courses/{courseId}")
    suspend fun getDetailCourse(
        @Path("courseId") courseId: Long
    ): BaseResponse<CourseResponse>

    @POST("courses")
    suspend fun postCourse(
        @Body courseRequest: CourseRequest
    ): BaseResponse<CourseResponse>

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(
        @Path("courseId") courseId: Long
    ): BaseResponse<Unit>

    @PUT("courses/{courseId}/items/{itemId}")
    suspend fun putCourseItem(
        @Path("courseId") courseId: Long,
        @Path("itemId") itemId: Long,
        @Body courseItemUpdateRequest: CourseItemUpdateRequest
    ): BaseResponse<CourseResponse>

    @GET("courses/{courseId}/items/{itemId}/alternatives")
    suspend fun getCourseItemAlternatives(
        @Path("courseId") courseId: Long,
        @Path("itemId") itemId: Long
    ): BaseResponse<List<AlternativePlaceResponse>>

    @PATCH("courses/{courseId}/name")
    suspend fun patchCourseName(
        @Path("courseId") courseId: Long,
        @Body courseNameRequest: CourseNameRequest
    ): BaseResponse<CourseResponse>

    @PATCH("courses/{courseId}/items/reorder")
    suspend fun patchCourseItemsReorder(
        @Path("courseId") courseId: Long,
        @Body courseItemReorderRequest: CourseItemReorderRequest
    ): BaseResponse<CourseResponse>
}
