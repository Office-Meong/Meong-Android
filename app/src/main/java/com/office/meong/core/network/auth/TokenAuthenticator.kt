package com.office.meong.core.network.auth

import com.office.meong.core.localstorage.token.TokenManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_ATTEMPTS) {
            Timber.w("토큰 갱신 재시도 한도 초과. 인증 중단")
            return null
        }

        return runBlocking {
            mutex.withLock {
                val currentToken = tokenManager.getAccessToken()

                // 이미 갱신되었는지 확인
                val reqToken = response.request.header("Authorization")?.substringAfter("Bearer ")
                if (reqToken != currentToken && currentToken != null) {
                    Timber.d("토큰 이미 갱신됨. 새 토큰으로 재시도")
                    return@withLock response.request.newBuilder()
                        .header("Authorization", "Bearer $currentToken")
                        .build()
                }

                val refreshToken = tokenManager.getRefreshToken() ?: return@withLock null // 로그아웃 필요

                when (val result = tokenRefreshService.refresh(refreshToken)) {
                    is TokenRefreshResult.Success -> {
                        tokenManager.saveTokens(result.accessToken.value, result.refreshToken.value)

                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${result.accessToken.value}")
                            .build()
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
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    companion object {
        private const val MAX_AUTH_ATTEMPTS = 3
    }
}