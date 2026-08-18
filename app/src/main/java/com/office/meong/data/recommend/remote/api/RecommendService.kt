package com.office.meong.data.recommend.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.recommend.remote.dto.response.PlaceRecommendationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendService {
    @GET("recommend")
    suspend fun getRecommendedPlaces(
        @Query("region") region: String,
        @Query("dogId") dogId: Long?
    ): BaseResponse<List<PlaceRecommendationResponse>>
}
