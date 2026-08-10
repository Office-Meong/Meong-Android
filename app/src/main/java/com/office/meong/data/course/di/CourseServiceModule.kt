package com.office.meong.data.course.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.course.remote.api.CourseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CourseServiceModule {
    @Provides
    @Singleton
    fun provideCourseService(@AuthNetwork retrofit: Retrofit): CourseService =
        retrofit.create()
}
