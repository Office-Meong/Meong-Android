package com.office.meong.data.auth.remote.datasource

import com.office.meong.core.network.di.AuthNetwork
import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.auth.remote.api.AuthService
import com.office.meong.data.auth.remote.dto.request.KakaoLoginRequest
import com.office.meong.data.auth.remote.dto.request.TokenRefreshRequest
import com.office.meong.data.auth.remote.dto.response.TokenResponse
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    @NoAuthNetwork private val noAuthAuthService: AuthService,
    @AuthNetwork private val authService: AuthService,
) {
    suspend fun loginWithKakao(
        code: String,
        termsAgreed: Boolean,
        privacyAgreed: Boolean,
    ): TokenResponse = noAuthAuthService.loginWithKakao(
        KakaoLoginRequest(code = code, termsAgreed = termsAgreed, privacyAgreed = privacyAgreed)
    ).getOrThrow()

    suspend fun logout() {
        authService.logout().getOrThrow()
    }

    suspend fun refreshToken(refreshToken: String): TokenResponse =
        noAuthAuthService.refreshToken(TokenRefreshRequest(refreshToken)).getOrThrow()
}
