package com.office.meong.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.extension.isHttpUnauthorized
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.data.auth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _sideEffect = Channel<SplashSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        checkLoginState()
    }

    private fun checkLoginState() {
        viewModelScope.launch {
            delay(2.seconds)
            val refreshToken = tokenManager.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                _sideEffect.send(SplashSideEffect.NavigateToLogin)
                return@launch
            }

            authRepository.refreshToken(refreshToken)
                .onSuccess { token ->
                    tokenManager.saveTokens(token.accessToken, token.refreshToken)
                    _sideEffect.send(SplashSideEffect.NavigateToHome)
                }
                .onFailure { throwable ->
                    if (throwable.isHttpUnauthorized()) {
                        tokenManager.clearTokens()
                        _sideEffect.send(SplashSideEffect.NavigateToLogin)
                    } else {
                        // 네트워크 등 일시적 오류 — 토큰은 유지하고 일단 진입시킨다.
                        // 이후 실제 API 호출에서 TokenAuthenticator가 재발급을 다시 시도한다.
                        _sideEffect.send(SplashSideEffect.NavigateToHome)
                    }
                }
        }
    }
}
