package com.office.meong.data.recommend.di

import com.office.meong.data.recommend.repository.RecommendRepository
import com.office.meong.data.recommend.repositoryimpl.RecommendRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RecommendRepositoryModule {
    @Binds
    @Singleton
    fun bindRecommendRepository(
        recommendRepositoryImpl: RecommendRepositoryImpl
    ): RecommendRepository
}
