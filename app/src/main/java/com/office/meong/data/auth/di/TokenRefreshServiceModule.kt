package com.office.meong.data.auth.di

import com.office.meong.core.network.auth.TokenRefreshService
import com.office.meong.data.auth.serviceimpl.TokenRefreshServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenRefreshServiceModule {

    @Binds
    @Singleton
    abstract fun bindTokenRefreshService(tokenRefreshServiceImpl: TokenRefreshServiceImpl): TokenRefreshService
}
