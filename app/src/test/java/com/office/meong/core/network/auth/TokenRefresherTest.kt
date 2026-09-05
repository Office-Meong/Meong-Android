package com.office.meong.core.network.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TokenRefresherTest {

    @Test
    fun `여러 요청이 동시에 갱신을 시도해도 refresh는 한 번만 나간다`() = runBlocking(Dispatchers.Default) {
        val tokenManager = FakeTokenManager(accessToken = "old-access")
        val service = FakeTokenRefreshService()
        val refresher = TokenRefresher(tokenManager, service)

        val results = List(20) {
            async { refresher.refreshIfNeeded("old-access") }
        }.awaitAll()

        assertEquals(1, service.callCount)
        assertTrue(results.all { it == "new-access-1" })
        assertEquals("new-access-1", tokenManager.access)
    }

    @Test
    fun `다른 요청이 이미 갱신을 마쳤으면 재갱신 없이 현재 토큰으로 재시도한다`() = runBlocking {
        val tokenManager = FakeTokenManager(accessToken = "already-new")
        val service = FakeTokenRefreshService()
        val refresher = TokenRefresher(tokenManager, service)

        val token = refresher.refreshIfNeeded(requestToken = "stale-token")

        assertEquals("already-new", token)
        assertEquals(0, service.callCount)
    }

    @Test
    fun `refresh token이 만료되면 저장된 토큰을 삭제하고 null을 반환한다`() = runBlocking {
        val tokenManager = FakeTokenManager(accessToken = "old-access")
        val service = FakeTokenRefreshService(resultFor = { TokenRefreshResult.Unauthorized })
        val refresher = TokenRefresher(tokenManager, service)

        val token = refresher.refreshIfNeeded("old-access")

        assertNull(token)
        assertTrue(tokenManager.cleared)
    }

    @Test
    fun `네트워크 등 일시적 오류면 토큰을 유지하고 null을 반환한다`() = runBlocking {
        val tokenManager = FakeTokenManager(accessToken = "old-access")
        val service = FakeTokenRefreshService(
            resultFor = { TokenRefreshResult.Failure(RuntimeException("timeout")) }
        )
        val refresher = TokenRefresher(tokenManager, service)

        val token = refresher.refreshIfNeeded("old-access")

        assertNull(token)
        assertEquals("old-access", tokenManager.access)
        assertFalse(tokenManager.cleared)
    }

    @Test
    fun `refresh token이 없으면 갱신을 시도하지 않고 null을 반환한다`() = runBlocking {
        val tokenManager = FakeTokenManager(accessToken = "old-access", refreshToken = null)
        val service = FakeTokenRefreshService()
        val refresher = TokenRefresher(tokenManager, service)

        val token = refresher.refreshIfNeeded("old-access")

        assertNull(token)
        assertEquals(0, service.callCount)
    }
}
