package com.office.meong.data.recommend.repository

import com.office.meong.core.model.region.Region
import com.office.meong.data.recommend.model.PlaceRecommendation

interface RecommendRepository {
    suspend fun getRecommendedPlaces(region: Region, dogId: Long? = null): Result<List<PlaceRecommendation>>
}
