package com.office.meong.data.policy.model

import com.office.meong.data.policy.remote.dto.response.PolicyResponse

data class PolicyModel(
    val termsUrl: String,
    val privacyUrl: String,
    val inquiryUrl: String
)

fun PolicyResponse.toModel() = PolicyModel(
    termsUrl = termsUrl,
    privacyUrl = privacyUrl,
    inquiryUrl = inquiryUrl
)