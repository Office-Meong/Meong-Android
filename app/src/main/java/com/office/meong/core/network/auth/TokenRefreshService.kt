package com.office.meong.core.network.auth

import com.office.meong.core.model.auth.AccessToken
import com.office.meong.core.model.auth.RefreshToken

interface TokenRefreshService {
    suspend fun refresh(refreshToken: String): TokenRefreshResult
}

sealed interface TokenRefreshResult {
    data class Success(
        val accessToken: AccessToken,
        val refreshToken: RefreshToken
    ) : TokenRefreshResult

    /** refresh token 자체가 만료·무효(401/403) — 저장된 토큰을 삭제하고 재로그인해야 한다 */
    data object Unauthorized : TokenRefreshResult

    /** 네트워크 등 일시적 오류 — 토큰은 유지하고 이번 요청만 실패시킨다 */
    data class Failure(val cause: Throwable) : TokenRefreshResult
}