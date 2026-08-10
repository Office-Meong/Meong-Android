package com.office.meong.data.course.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.course.remote.dto.response.CourseSummaryResponse
import retrofit2.http.GET

interface CourseService {

    @GET("courses")
    suspend fun getCourses(): BaseResponse<List<CourseSummaryResponse>>
}
