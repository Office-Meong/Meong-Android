package com.office.meong.core.network.auth

import com.office.meong.core.localstorage.token.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenManager.getAccessToken()
        }

        val newRequest = chain.request().newBuilder().apply {
            if (token != null) header("Authorization", "Bearer $token")
        }.build()

        return chain.proceed(newRequest)
    }
}
