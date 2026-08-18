package com.office.meong.data.walkcourse.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.walkcourse.model.WalkCourse
import com.office.meong.data.walkcourse.model.toModel
import com.office.meong.data.walkcourse.remote.datasource.WalkCourseDataSource
import com.office.meong.data.walkcourse.repository.WalkCourseRepository
import javax.inject.Inject

class WalkCourseRepositoryImpl @Inject constructor(
    private val walkCourseDataSource: WalkCourseDataSource,
) : WalkCourseRepository {
    override suspend fun getWalkCourses(
        latitude: Double,
        longitude: Double,
        dogId: Long?
    ): Result<List<WalkCourse>> = suspendRunCatching {
        walkCourseDataSource.getWalkCourses(latitude, longitude, dogId).map { it.toModel() }
    }
}
