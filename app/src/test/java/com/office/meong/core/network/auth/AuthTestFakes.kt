package com.office.meong.core.network.auth

import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.core.model.auth.AccessToken
import com.office.meong.core.model.auth.RefreshToken
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

/**
 * 401/403 토큰 갱신 흐름을 검증하는 단위 테스트에서 함께 쓰는 Fake 구현.
 */
internal class FakeTokenManager(
    accessToken: String? = "old-access",
    refreshToken: String? = "refresh-1",
) : TokenManager {
    @Volatile
    var access: String? = accessToken

    @Volatile
    var refresh: String? = refreshToken

    @Volatile
    var cleared: Boolean = false

    override suspend fun saveAccessToken(token: String) {
        access = token
    }

    override suspend fun saveRefreshToken(token: String) {
        refresh = token
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        access = accessToken
        refresh = refreshToken
    }

    override suspend fun getAccessToken(): String? = access

    override suspend fun getRefreshToken(): String? = refresh

    override suspend fun clearTokens() {
        access = null
        refresh = null
        cleared = true
    }
}

internal class FakeTokenRefreshService(
    private val delayMillis: Long = 30L,
    private val resultFor: (attempt: Int) -> TokenRefreshResult = { attempt ->
        TokenRefreshResult.Success(
            accessToken = AccessToken("new-access-$attempt"),
            refreshToken = RefreshToken("new-refresh-$attempt"),
        )
    },
) : TokenRefreshService {
    private val calls = AtomicInteger(0)

    val callCount: Int get() = calls.get()

    override suspend fun refresh(refreshToken: String): TokenRefreshResult {
        val attempt = calls.incrementAndGet()
        delay(delayMillis)
        return resultFor(attempt)
    }
}
