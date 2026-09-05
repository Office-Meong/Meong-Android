package com.office.meong.data.review.di

import com.office.meong.data.review.repository.ReviewRepository
import com.office.meong.data.review.repositoryimpl.ReviewRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ReviewRepositoryModule {
    @Binds
    @Singleton
    fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository
}
