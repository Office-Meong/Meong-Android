package com.office.meong.data.policy.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.policy.remote.dto.response.PolicyResponse
import retrofit2.http.GET

interface PolicyService {
    @GET("app/policies")
    suspend fun getPolicies(): BaseResponse<PolicyResponse>
}