package com.office.meong.data.geocoding.di

import com.office.meong.data.geocoding.repository.GeocodingRepository
import com.office.meong.data.geocoding.repositoryimpl.GeocodingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GeocodingRepositoryModule {
    @Binds
    @Singleton
    fun bindGeocodingRepository(
        geocodingRepositoryImpl: GeocodingRepositoryImpl
    ): GeocodingRepository
}
