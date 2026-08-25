package com.office.meong.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.extension.isHttpUnauthorized
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.data.auth.repository.AuthRepository
import com.office.meong.data.pet.repository.PetRepository
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
    private val petRepository: PetRepository,
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
                _sideEffect.send(SplashSideEffect.NavigateToLogin())
                return@launch
            }

            authRepository.refreshToken(refreshToken)
                .onSuccess { token ->
                    tokenManager.saveTokens(token.accessToken, token.refreshToken)
                    val hasNoDogs = petRepository.getDogs().getOrDefault(emptyList()).isEmpty()
                    _sideEffect.send(if (hasNoDogs) SplashSideEffect.NavigateToSignup else SplashSideEffect.NavigateToHome)
                }
                .onFailure { throwable ->
                    if (throwable.isHttpUnauthorized()) {
                        // 리프레시 토큰 만료(세션 만료) — 이미 약관 동의를 마친 회원이므로 재로그인 시 바텀시트는 건너뛴다.
                        tokenManager.clearTokens()
                        _sideEffect.send(SplashSideEffect.NavigateToLogin(skipTermsBottomSheet = true))
                    } else {
                        // 네트워크 등 일시적 오류 — 토큰은 유지하고 일단 진입시킨다.
                        // 이후 실제 API 호출에서 TokenAuthenticator가 재발급을 다시 시도한다.
                        _sideEffect.send(SplashSideEffect.NavigateToHome)
                    }
                }
        }
    }
}
