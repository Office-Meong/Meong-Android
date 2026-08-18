package com.office.meong.data.walkcourse.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.walkcourse.remote.dto.response.WalkCourseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WalkCourseService {
    @GET("walk-courses")
    suspend fun getWalkCourses(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("dogId") dogId: Long?
    ): BaseResponse<List<WalkCourseResponse>>
}
