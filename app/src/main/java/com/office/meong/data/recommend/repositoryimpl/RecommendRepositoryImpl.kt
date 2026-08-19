package com.office.meong.data.recommend.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.core.model.region.Region
import com.office.meong.data.recommend.model.PlaceRecommendation
import com.office.meong.data.recommend.model.toModel
import com.office.meong.data.recommend.remote.datasource.RecommendDataSource
import com.office.meong.data.recommend.repository.RecommendRepository
import javax.inject.Inject

class RecommendRepositoryImpl @Inject constructor(
    private val recommendDataSource: RecommendDataSource,
) : RecommendRepository {
    override suspend fun getRecommendedPlaces(
        region: Region,
        dogId: Long?
    ): Result<List<PlaceRecommendation>> = suspendRunCatching {
        recommendDataSource.getRecommendedPlaces(region.name, dogId).map { it.toModel() }
    }
}
