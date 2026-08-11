package com.office.meong.data.course.di

import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.course.repositoryimpl.CourseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CourseRepositoryModule {

    @Binds
    @Singleton
    fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository
}
