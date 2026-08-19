package com.office.meong.data.walkcourse.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.walkcourse.remote.api.WalkCourseService
import com.office.meong.data.walkcourse.remote.dto.response.WalkCourseResponse
import javax.inject.Inject

class WalkCourseDataSource @Inject constructor(
    private val walkCourseService: WalkCourseService
) {
    suspend fun getWalkCourses(lat: Double, lng: Double, dogId: Long?): List<WalkCourseResponse> =
        walkCourseService.getWalkCourses(lat, lng, dogId).getOrThrow()
}
