package com.office.meong.data.recommend.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.recommend.remote.api.RecommendService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecommendServiceModule {
    @Provides
    @Singleton
    fun provideRecommendService(@AuthNetwork retrofit: Retrofit): RecommendService =
        retrofit.create()
}
