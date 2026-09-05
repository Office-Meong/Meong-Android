package com.office.meong.data.auth.repositoryimpl

import android.content.Context
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.auth.local.datasource.KakaoAuthDataSource
import com.office.meong.data.auth.model.TokenModel
import com.office.meong.data.auth.model.toModel
import com.office.meong.data.auth.remote.datasource.AuthDataSource
import com.office.meong.data.auth.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val kakaoAuthDataSource: KakaoAuthDataSource,
) : AuthRepository {
    override suspend fun getKakaoAuthorizationCode(context: Context): Result<String> =
        kakaoAuthDataSource.getAuthorizationCode(context)

    override suspend fun loginWithKakao(
        code: String,
        termsAgreed: Boolean,
        privacyAgreed: Boolean,
    ): Result<TokenModel> = suspendRunCatching {
        authDataSource.loginWithKakao(code, termsAgreed, privacyAgreed).toModel()
    }

    override suspend fun logout(): Result<Unit> = suspendRunCatching {
        authDataSource.logout()
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenModel> = suspendRunCatching {
        authDataSource.refreshToken(refreshToken).toModel()
    }
}
