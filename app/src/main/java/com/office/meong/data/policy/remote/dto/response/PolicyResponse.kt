package com.office.meong.data.policy.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PolicyResponse(
    @SerialName("termsUrl")
    val termsUrl: String,
    @SerialName("privacyUrl")
    val privacyUrl: String,
    @SerialName("inquiryUrl")
    val inquiryUrl: String
)