package com.office.meong.data.policy.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.policy.remote.api.PolicyService
import com.office.meong.data.policy.remote.dto.response.PolicyResponse
import javax.inject.Inject

class PolicyDataSource @Inject constructor(
    private val policyService: PolicyService
) {
    suspend fun getPolicies(): PolicyResponse = policyService.getPolicies().getOrThrow()
}
