package com.office.meong.data.auth.local.datasource

import android.content.Context
import com.kakao.sdk.auth.AuthCodeClient
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class KakaoAuthDataSource @Inject constructor() {
    suspend fun getAuthorizationCode(context: Context): Result<String> = suspendCancellableCoroutine { continuation ->
        val accountCallback: (String?, Throwable?) -> Unit = { code, error ->
            when {
                error != null -> continuation.resume(Result.failure(error))
                code != null -> continuation.resume(Result.success(code))
                else -> continuation.resume(Result.failure(IllegalStateException("카카오 인가코드 응답이 비어있습니다")))
            }
        }

        if (AuthCodeClient.instance.isKakaoTalkLoginAvailable(context)) {
            AuthCodeClient.instance.authorizeWithKakaoTalk(context) { code, error ->
                when {
                    // 카카오톡 인증 취소는 계정 로그인으로 폴백하지 않고 그대로 취소 처리한다
                    error != null && error is ClientError && error.reason == ClientErrorCause.Cancelled ->
                        continuation.resume(Result.failure(error))

                    error != null -> {
                        Timber.w(error, "카카오톡 인가코드 발급 실패, 계정 로그인으로 전환")
                        AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = accountCallback)
                    }

                    code != null -> continuation.resume(Result.success(code))
                    else -> continuation.resume(Result.failure(IllegalStateException("카카오 인가코드 응답이 비어있습니다")))
                }
            }
        } else {
            AuthCodeClient.instance.authorizeWithKakaoAccount(context, callback = accountCallback)
        }
    }
}
