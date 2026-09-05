package com.office.meong.data.walkcourse.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.walkcourse.remote.dto.response.WalkCourseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WalkCourseService {
    /**
     * 위치 기반 산책 코스 추천 API (두루누비 코스 데이터)
     * */
    @GET("walk-courses")
    suspend fun getWalkCourses(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("dogId") dogId: Long?
    ): BaseResponse<List<WalkCourseResponse>>
}
