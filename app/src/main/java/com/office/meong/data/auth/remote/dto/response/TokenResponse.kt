package com.office.meong.data.auth.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("tokenType")
    val tokenType: String,
    @SerialName("accessTokenExpiresIn")
    val accessTokenExpiresIn: Long,
)
