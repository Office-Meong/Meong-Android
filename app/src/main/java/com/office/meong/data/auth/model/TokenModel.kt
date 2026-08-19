package com.office.meong.data.auth.model

import com.office.meong.data.auth.remote.dto.response.TokenResponse

data class TokenModel(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
)

fun TokenResponse.toModel() = TokenModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
    accessTokenExpiresIn = accessTokenExpiresIn,
)
