package com.office.meong.data.place.di

import com.office.meong.data.place.repository.PlaceRepository
import com.office.meong.data.place.repositoryimpl.PlaceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PlaceRepositoryModule {
    @Binds
    @Singleton
    fun bindPlaceRepository(
        placeRepositoryImpl: PlaceRepositoryImpl
    ): PlaceRepository
}
