package com.office.meong.core.network.auth

import com.office.meong.core.localstorage.token.TokenManager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 401(Authenticator)과 403(Interceptor) 두 트리거가 동시에 갱신을 시도해도
 * 한 번만 갱신되도록 mutex로 직렬화한다.
 */
@Singleton
class TokenRefresher @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) {
    private val mutex = Mutex()

    suspend fun refreshIfNeeded(requestToken: String?): String? = mutex.withLock {
        val currentToken = tokenManager.getAccessToken()

        // 이미 다른 요청이 갱신을 마쳤다면 그 토큰으로 재시도
        if (requestToken != currentToken && currentToken != null) {
            Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도")
            return@withLock currentToken
        }

        val refreshToken = tokenManager.getRefreshToken() ?: return@withLock null // 로그아웃 필요

        when (val result = tokenRefreshService.refresh(refreshToken)) {
            is TokenRefreshResult.Success -> {
                tokenManager.saveTokens(result.accessToken.value, result.refreshToken.value)
                result.accessToken.value
            }

            is TokenRefreshResult.Unauthorized -> {
                tokenManager.clearTokens()
                null
            }

            is TokenRefreshResult.Failure -> {
                Timber.w(result.cause, "토큰 갱신 일시 실패. 토큰 유지")
                null
            }
        }
    }
}
