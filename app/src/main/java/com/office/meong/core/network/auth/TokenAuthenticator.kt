package com.office.meong.core.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_ATTEMPTS) {
            Timber.w("토큰 갱신 재시도 한도 초과. 인증 중단")
            return null
        }

        val reqToken = response.request.header("Authorization")?.substringAfter("Bearer ")
        val newToken = runBlocking { tokenRefresher.refreshIfNeeded(reqToken) } ?: return null

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
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
