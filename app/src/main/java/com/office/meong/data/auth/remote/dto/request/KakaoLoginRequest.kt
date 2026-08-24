package com.office.meong.data.auth.remote.dto.request

import com.office.meong.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequest(
    @SerialName("code")
    val code: String,
    @SerialName("clientId")
    val clientId: String = BuildConfig.KAKAO_NATIVE_APP_KEY,
    @SerialName("redirectUri")
    val redirectUri: String = "kakao${BuildConfig.KAKAO_NATIVE_APP_KEY}://oauth",
    @SerialName("termsAgreed")
    val termsAgreed: Boolean,
    @SerialName("privacyAgreed")
    val privacyAgreed: Boolean,
)
