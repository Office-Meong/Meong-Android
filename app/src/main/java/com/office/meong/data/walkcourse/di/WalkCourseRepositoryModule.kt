package com.office.meong.data.walkcourse.di

import com.office.meong.data.walkcourse.repository.WalkCourseRepository
import com.office.meong.data.walkcourse.repositoryimpl.WalkCourseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WalkCourseRepositoryModule {
    @Binds
    @Singleton
    fun bindWalkCourseRepository(
        walkCourseRepositoryImpl: WalkCourseRepositoryImpl
    ): WalkCourseRepository
}
