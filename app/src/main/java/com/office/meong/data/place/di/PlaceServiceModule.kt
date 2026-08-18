package com.office.meong.data.place.di

import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.data.place.remote.api.PlaceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceServiceModule {
    @Provides
    @Singleton
    fun providePlaceService(@NoAuthNetwork retrofit: Retrofit): PlaceService =
        retrofit.create()
}
