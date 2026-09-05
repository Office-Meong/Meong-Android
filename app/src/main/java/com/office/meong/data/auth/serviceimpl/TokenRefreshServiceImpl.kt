package com.office.meong.data.auth.serviceimpl

import com.office.meong.core.common.extension.isHttpUnauthorized
import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.core.model.auth.AccessToken
import com.office.meong.core.model.auth.RefreshToken
import com.office.meong.core.network.auth.TokenRefreshResult
import com.office.meong.core.network.auth.TokenRefreshService
import com.office.meong.core.network.di.NoAuthNetwork
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.auth.remote.api.AuthService
import com.office.meong.data.auth.remote.dto.request.TokenRefreshRequest
import javax.inject.Inject

class TokenRefreshServiceImpl @Inject constructor(
    @NoAuthNetwork private val authService: AuthService,
) : TokenRefreshService {
    override suspend fun refresh(refreshToken: String): TokenRefreshResult =
        suspendRunCatching {
            authService.refreshToken(TokenRefreshRequest(refreshToken)).getOrThrow()
        }.fold(
            onSuccess = { token ->
                TokenRefreshResult.Success(
                    accessToken = AccessToken(token.accessToken),
                    refreshToken = RefreshToken(token.refreshToken)
                )
            },
            onFailure = { throwable ->
                if (throwable.isHttpUnauthorized()) {
                    TokenRefreshResult.Unauthorized
                } else {
                    TokenRefreshResult.Failure(throwable)
                }
            }
        )
}
