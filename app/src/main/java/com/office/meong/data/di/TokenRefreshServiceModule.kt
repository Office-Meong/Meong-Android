package com.office.meong.data.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TokenRefreshServiceModule {

    /*@Binds
    @Singleton
    abstract fun bindTokenRefreshService(tokenRefreshServiceImpl: TokenRefreshServiceImpl): TokenRefreshService
*/}
