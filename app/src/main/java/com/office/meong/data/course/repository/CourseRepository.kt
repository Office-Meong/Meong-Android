package com.office.meong.data.course.repository

import com.office.meong.data.course.model.CourseSummary

interface CourseRepository {
    suspend fun getCourses(): Result<List<CourseSummary>>
}
