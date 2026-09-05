package com.office.meong.data.auth.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.data.auth.remote.api.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthServiceModule {
    @Provides
    @Singleton
    @NoAuthNetwork
    fun provideNoAuthAuthService(@NoAuthNetwork retrofit: Retrofit): AuthService = retrofit.create()

    @Provides
    @Singleton
    @AuthNetwork
    fun provideAuthAuthService(@AuthNetwork retrofit: Retrofit): AuthService = retrofit.create()
}
