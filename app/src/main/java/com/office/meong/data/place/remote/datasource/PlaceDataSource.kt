package com.office.meong.data.place.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.place.remote.api.PlaceService
import com.office.meong.data.place.remote.dto.response.PlaceDetailResponse
import com.office.meong.data.place.remote.dto.response.PlacePageResponse
import com.office.meong.data.place.remote.dto.response.PlaceWalkCourseResponse
import javax.inject.Inject

class PlaceDataSource @Inject constructor(
    private val placeService: PlaceService
) {
    suspend fun getPlaces(
        region: String?,
        type: String?,
        sort: String?,
        congestion: String?,
        keyword: String?,
        page: Int,
        size: Int,
    ): PlacePageResponse =
        placeService.getPlaces(region, type, sort, congestion, keyword, page, size).getOrThrow()

    suspend fun getPlaceDetail(id: Long): PlaceDetailResponse =
        placeService.getPlaceDetail(id).getOrThrow()

    suspend fun getPlaceWalkCourses(id: Long): List<PlaceWalkCourseResponse> =
        placeService.getPlaceWalkCourses(id).getOrThrow()
}
