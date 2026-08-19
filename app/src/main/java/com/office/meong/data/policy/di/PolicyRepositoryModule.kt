package com.office.meong.data.policy.di

import com.office.meong.data.policy.repository.PolicyRepository
import com.office.meong.data.policy.repositoryimpl.PolicyRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface PolicyRepositoryModule {
    @Binds
    @Singleton
    fun bindPolicyRepository(
        policyRepositoryImpl: PolicyRepositoryImpl
    ): PolicyRepository
}