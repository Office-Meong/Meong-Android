package com.office.meong.data.recommend.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.recommend.remote.api.RecommendService
import com.office.meong.data.recommend.remote.dto.response.PlaceRecommendationResponse
import javax.inject.Inject

class RecommendDataSource @Inject constructor(
    private val recommendService: RecommendService
) {
    suspend fun getRecommendedPlaces(region: String, dogId: Long?): List<PlaceRecommendationResponse> =
        recommendService.getRecommendedPlaces(region, dogId).getOrThrow()
}
