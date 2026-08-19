package com.office.meong.data.auth.repository

import android.content.Context
import com.office.meong.data.auth.model.TokenModel

interface AuthRepository {
    suspend fun getKakaoAuthorizationCode(context: Context): Result<String>
    suspend fun loginWithKakao(code: String, termsAgreed: Boolean, privacyAgreed: Boolean): Result<TokenModel>
    suspend fun logout(): Result<Unit>
    suspend fun refreshToken(refreshToken: String): Result<TokenModel>
}
