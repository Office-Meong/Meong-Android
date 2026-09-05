package com.office.meong.data.review.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.review.remote.api.ReviewService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReviewServiceModule {
    @Provides
    @Singleton
    fun provideReviewService(@AuthNetwork retrofit: Retrofit): ReviewService =
        retrofit.create()
}
