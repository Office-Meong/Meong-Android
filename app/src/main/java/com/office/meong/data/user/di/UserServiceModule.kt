package com.office.meong.data.user.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.user.remote.api.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserServiceModule {
    @Provides
    @Singleton
    fun provideUserService(@AuthNetwork retrofit: Retrofit): UserService =
        retrofit.create()
}
