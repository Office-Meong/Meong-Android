package com.office.meong.data.pet.di

import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.pet.repositoryimpl.PetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PetRepositoryModule {
    @Binds
    @Singleton
    fun bindPetRepository(
        petRepositoryImpl: PetRepositoryImpl
    ): PetRepository
}
