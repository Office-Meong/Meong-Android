package com.office.meong.data.policy.repositoryimpl

import com.office.meong.core.cache.InMemoryCache
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.policy.model.PolicyModel
import com.office.meong.data.policy.model.toModel
import com.office.meong.data.policy.remote.datasource.PolicyDataSource
import com.office.meong.data.policy.repository.PolicyRepository
import javax.inject.Inject

class PolicyRepositoryImpl @Inject constructor(
    private val policyDataSource: PolicyDataSource,
    private val policyCache: InMemoryCache<PolicyModel>,
) : PolicyRepository {
    override suspend fun getPolicies(): Result<PolicyModel> = suspendRunCatching {
        policyCache.getOrFetch { policyDataSource.getPolicies().toModel() }
    }
}