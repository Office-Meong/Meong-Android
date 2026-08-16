package com.office.meong.data.presigned.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.data.presigned.remote.api.PresignedService
import com.office.meong.data.presigned.remote.api.S3Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PresignedServiceModule {
    @Provides
    @Singleton
    fun providePresignedService(@AuthNetwork retrofit: Retrofit): PresignedService =
        retrofit.create()

    @Provides
    @Singleton
    fun provideS3Service(@NoAuthNetwork retrofit: Retrofit): S3Service =
        retrofit.create()
}
