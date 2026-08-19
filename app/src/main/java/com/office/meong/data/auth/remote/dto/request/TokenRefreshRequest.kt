package com.office.meong.data.auth.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshRequest(
    @SerialName("refreshToken")
    val refreshToken: String,
)
