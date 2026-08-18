package com.office.meong.data.walkcourse.repository

import com.office.meong.data.walkcourse.model.WalkCourse

interface WalkCourseRepository {
    suspend fun getWalkCourses(
        latitude: Double,
        longitude: Double,
        dogId: Long? = null
    ): Result<List<WalkCourse>>
}
