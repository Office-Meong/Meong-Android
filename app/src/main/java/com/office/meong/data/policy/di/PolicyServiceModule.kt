package com.office.meong.data.policy.di

import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.data.policy.remote.api.PolicyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PolicyServiceModule {
    @Provides
    @Singleton
    fun providePolicyService(@NoAuthNetwork retrofit: Retrofit): PolicyService =
        retrofit.create()
}