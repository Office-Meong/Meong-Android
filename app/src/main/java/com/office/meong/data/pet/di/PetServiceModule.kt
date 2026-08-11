package com.office.meong.data.pet.di

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.data.pet.remote.api.PetService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PetServiceModule {
    @Provides
    @Singleton
    fun providePetService(@AuthNetwork retrofit: Retrofit): PetService =
        retrofit.create()
}
