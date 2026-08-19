package com.office.meong.data.walkcourse.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.walkcourse.remote.api.WalkCourseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WalkCourseServiceModule {
    @Provides
    @Singleton
    fun provideWalkCourseService(@AuthNetwork retrofit: Retrofit): WalkCourseService =
        retrofit.create()
}
