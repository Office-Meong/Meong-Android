package com.office.meong.data.auth.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    @SerialName("code")
    val code: String,
    @SerialName("termsAgreed")
    val termsAgreed: Boolean,
    @SerialName("privacyAgreed")
    val privacyAgreed: Boolean,
)
