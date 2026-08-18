package com.office.meong.data.place.repository

import com.office.meong.core.model.region.Region
import com.office.meong.data.place.model.PlaceDetail
import com.office.meong.data.place.model.PlacePage
import com.office.meong.data.place.model.PlaceWalkCourse

interface PlaceRepository {
    suspend fun getPlaces(
        region: Region? = null,
        type: String? = null,
        sort: String? = null,
        congestion: String? = null,
        keyword: String? = null,
        page: Int = 0,
        size: Int = 20,
    ): Result<PlacePage>

    suspend fun getPlaceDetail(placeId: Long): Result<PlaceDetail>

    suspend fun getPlaceWalkCourses(placeId: Long): Result<List<PlaceWalkCourse>>
}
