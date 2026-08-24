package com.office.meong.data.geocoding.di

import com.office.meong.core.network.di.KakaoLocalNetwork
import com.office.meong.data.geocoding.remote.api.KakaoLocalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeocodingServiceModule {
    @Provides
    @Singleton
    fun provideKakaoLocalService(@KakaoLocalNetwork retrofit: Retrofit): KakaoLocalService =
        retrofit.create()
}
