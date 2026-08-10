package com.office.meong.data.auth.serviceimpl

import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.core.network.auth.TokenRefreshResult
import com.office.meong.core.network.auth.TokenRefreshService
import javax.inject.Inject

class TokenRefreshServiceImpl @Inject constructor(
    //private val reissueService: ReissueService,
    private val tokenManager: TokenManager
) : TokenRefreshService {
    override suspend fun refresh(refreshToken: String): TokenRefreshResult {
        TODO("Not yet implemented")
    }

}