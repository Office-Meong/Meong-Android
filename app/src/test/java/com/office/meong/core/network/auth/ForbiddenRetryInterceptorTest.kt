package com.office.meong.core.network.auth

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 403 응답에 대해 [ForbiddenRetryInterceptor] 가 토큰을 갱신하고 한 번만 재시도하는지 검증한다.
 *
 * 실제 네트워크 대신 체인 맨 끝의 [ServerStub] 인터셉터가 정해진 응답을 돌려주므로
 * [ForbiddenRetryInterceptor] 에는 OkHttp 의 실제 Chain 이 전달된다.
 */
class ForbiddenRetryInterceptorTest {

    private fun request(token: String = "old-access"): Request =
        Request.Builder()
            .url("https://pettravel.kr/api/test")
            .header("Authorization", "Bearer $token")
            .build()

    private fun clientWith(
        interceptor: ForbiddenRetryInterceptor,
        stub: ServerStub,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(stub)
        .build()

    @Test
    fun `403이 아니면 갱신 없이 응답을 그대로 통과시킨다`() {
        val service = FakeTokenRefreshService()
        val interceptor = ForbiddenRetryInterceptor(TokenRefresher(FakeTokenManager(), service))
        val stub = ServerStub(codes = listOf(200))

        val response = clientWith(interceptor, stub).newCall(request()).execute()

        assertEquals(200, response.code)
        assertEquals(1, stub.receivedRequests.size)
        assertEquals(0, service.callCount)
    }

    @Test
    fun `403이면 토큰을 갱신해 새 토큰으로 한 번 재시도한다`() {
        val service = FakeTokenRefreshService()
        val interceptor = ForbiddenRetryInterceptor(TokenRefresher(FakeTokenManager(), service))
        val stub = ServerStub(codes = listOf(403, 200))

        val response = clientWith(interceptor, stub).newCall(request()).execute()

        assertEquals(200, response.code)
        assertEquals(2, stub.receivedRequests.size)
        assertEquals(1, service.callCount)
        assertEquals(
            "Bearer new-access-1",
            stub.receivedRequests[1].header("Authorization"),
        )
    }

    @Test
    fun `재시도도 403이면 무한 재시도 없이 두 번째 응답을 반환한다`() {
        val service = FakeTokenRefreshService()
        val interceptor = ForbiddenRetryInterceptor(TokenRefresher(FakeTokenManager(), service))
        val stub = ServerStub(codes = listOf(403))

        val response = clientWith(interceptor, stub).newCall(request()).execute()

        assertEquals(403, response.code)
        assertEquals(2, stub.receivedRequests.size)
        assertEquals(1, service.callCount)
    }

    @Test
    fun `refresh token이 없어 갱신에 실패하면 원래 403 응답을 그대로 반환한다`() {
        val service = FakeTokenRefreshService()
        val tokenManager = FakeTokenManager(accessToken = "old-access", refreshToken = null)
        val interceptor = ForbiddenRetryInterceptor(TokenRefresher(tokenManager, service))
        val stub = ServerStub(codes = listOf(403))

        val response = clientWith(interceptor, stub).newCall(request()).execute()

        assertEquals(403, response.code)
        assertEquals(1, stub.receivedRequests.size)
        assertEquals(0, service.callCount)
    }
}

/**
 * 체인 맨 끝에서 네트워크 대신 정해진 상태 코드를 순서대로 돌려주는 인터셉터.
 * [codes] 를 넘어서는 호출은 마지막 코드를 재사용한다.
 */
private class ServerStub(private val codes: List<Int>) : Interceptor {

    val receivedRequests = mutableListOf<Request>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        receivedRequests += request
        val code = codes.getOrElse(receivedRequests.size - 1) { codes.last() }
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(if (code == 403) "Forbidden" else "OK")
            .body("".toResponseBody())
            .build()
    }
}
