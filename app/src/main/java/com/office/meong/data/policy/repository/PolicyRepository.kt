package com.office.meong.data.policy.repository

import com.office.meong.data.policy.model.PolicyModel

interface PolicyRepository {
    suspend fun getPolicies(): Result<PolicyModel>
}