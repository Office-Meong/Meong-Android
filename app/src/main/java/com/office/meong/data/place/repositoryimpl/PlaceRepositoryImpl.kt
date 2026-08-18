package com.office.meong.data.place.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.core.model.region.Region
import com.office.meong.data.place.model.PlaceDetail
import com.office.meong.data.place.model.PlacePage
import com.office.meong.data.place.model.PlaceWalkCourse
import com.office.meong.data.place.model.toModel
import com.office.meong.data.place.remote.datasource.PlaceDataSource
import com.office.meong.data.place.repository.PlaceRepository
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDataSource: PlaceDataSource,
) : PlaceRepository {
    override suspend fun getPlaces(
        region: Region?,
        type: String?,
        sort: String?,
        congestion: String?,
        keyword: String?,
        page: Int,
        size: Int,
    ): Result<PlacePage> = suspendRunCatching {
        placeDataSource.getPlaces(region?.name, type, sort, congestion, keyword, page, size).toModel()
    }

    override suspend fun getPlaceDetail(placeId: Long): Result<PlaceDetail> = suspendRunCatching {
        placeDataSource.getPlaceDetail(placeId).toModel()
    }

    override suspend fun getPlaceWalkCourses(placeId: Long): Result<List<PlaceWalkCourse>> = suspendRunCatching {
        placeDataSource.getPlaceWalkCourses(placeId).map { it.toModel() }
    }
}
