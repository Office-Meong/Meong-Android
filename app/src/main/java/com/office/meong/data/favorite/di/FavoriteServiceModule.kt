package com.office.meong.data.favorite.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.favorite.remote.api.FavoriteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoriteServiceModule {
    @Provides
    @Singleton
    fun provideFavoriteService(@AuthNetwork retrofit: Retrofit): FavoriteService =
        retrofit.create()
}
