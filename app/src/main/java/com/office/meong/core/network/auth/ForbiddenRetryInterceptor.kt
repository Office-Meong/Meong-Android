package com.office.meong.core.network.auth

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp의 [okhttp3.Authenticator]는 401에서만 호출되므로
 * 403은 별도 인터셉터로 감지해 [TokenRefresher]로 갱신한 뒤 한 번만 재시도한다.
 * 결과적으로 이건 403용
 */
@Singleton
class ForbiddenRetryInterceptor @Inject constructor(
    private val tokenRefresher: TokenRefresher
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code != HTTP_FORBIDDEN) return response

        val reqToken = request.header("Authorization")?.substringAfter("Bearer ")
        val newToken = runBlocking { tokenRefresher.refreshIfNeeded(reqToken) } ?: return response

        response.close()

        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val HTTP_FORBIDDEN = 403
    }
}
