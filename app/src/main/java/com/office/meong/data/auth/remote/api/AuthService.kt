package com.office.meong.data.auth.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.auth.remote.dto.request.KakaoLoginRequest
import com.office.meong.data.auth.remote.dto.request.TokenRefreshRequest
import com.office.meong.data.auth.remote.dto.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/kakao")
    suspend fun loginWithKakao(
        @Body kakaoLoginRequest: KakaoLoginRequest
    ): BaseResponse<TokenResponse>

    @POST("auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body tokenRefreshRequest: TokenRefreshRequest
    ): BaseResponse<TokenResponse>
}
