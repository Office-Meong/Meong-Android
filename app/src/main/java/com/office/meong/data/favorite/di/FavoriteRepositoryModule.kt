package com.office.meong.data.favorite.di

import com.office.meong.data.favorite.repository.FavoriteRepository
import com.office.meong.data.favorite.repositoryimpl.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FavoriteRepositoryModule {
    @Binds
    @Singleton
    fun bindFavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ): FavoriteRepository
}
