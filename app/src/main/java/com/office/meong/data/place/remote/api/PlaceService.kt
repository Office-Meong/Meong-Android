package com.office.meong.data.place.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.place.remote.dto.response.PlaceDetailResponse
import com.office.meong.data.place.remote.dto.response.PlacePageResponse
import com.office.meong.data.place.remote.dto.response.PlaceWalkCourseResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceService {
    @GET("places")
    suspend fun getPlaces(
        @Query("region") region: String?,
        @Query("type") type: String?,
        @Query("sort") sort: String?,
        @Query("congestion") congestion: String?,
        @Query("keyword") keyword: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): BaseResponse<PlacePageResponse>

    @GET("places/{id}")
    suspend fun getPlaceDetail(@Path("id") id: Long): BaseResponse<PlaceDetailResponse>

    @GET("places/{id}/walk-courses")
    suspend fun getPlaceWalkCourses(@Path("id") id: Long): BaseResponse<List<PlaceWalkCourseResponse>>
}
