package com.office.meong.data.course.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.course.model.CourseSummary
import com.office.meong.data.course.model.toModel
import com.office.meong.data.course.remote.datasource.CourseDataSource
import com.office.meong.data.course.repository.CourseRepository
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val courseDataSource: CourseDataSource,
) : CourseRepository {

    override suspend fun getCourses(): Result<List<CourseSummary>> = suspendRunCatching {
        courseDataSource.getCourses().map { it.toModel() }
    }
}
