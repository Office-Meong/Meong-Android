package com.office.meong.data.presigned.di

import com.office.meong.data.presigned.repository.PresignedRepository
import com.office.meong.data.presigned.repositoryimpl.PresignedRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PresignedRepositoryModule {

    @Binds
    @Singleton
    fun bindPresignedRepository(impl: PresignedRepositoryImpl): PresignedRepository
}
