package com.office.meong.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.data.auth.repository.AuthRepository
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.policy.repository.PolicyRepository
import com.office.meong.presentation.auth.model.LoginSideEffect
import com.office.meong.presentation.auth.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val policyRepository: PolicyRepository,
    private val petRepository: PetRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _sideEffect = Channel<LoginSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private var kakaoAuthCode: String? = null

    fun onKakaoLoginClick(context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authRepository.getKakaoAuthorizationCode(context)
                .onSuccess { code ->
                    kakaoAuthCode = code
                    _state.update { it.copy(isLoading = false, isTermsBottomSheetVisible = true) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffect.send(LoginSideEffect.ShowToast("카카오 로그인에 실패했어요"))
                }
        }
    }

    fun onServiceTermClick() {
        _state.update { it.copy(isServiceTermAgreed = !it.isServiceTermAgreed) }
    }

    fun onPrivacyTermClick() {
        _state.update { it.copy(isPrivacyTermAgreed = !it.isPrivacyTermAgreed) }
    }

    fun onBottomSheetDismiss() {
        _state.update { it.copy(isTermsBottomSheetVisible = false) }
    }

    fun onViewServiceTermClick() {
        viewModelScope.launch {
            policyRepository.getPolicies()
                .onSuccess { openTermsUrl(it.termsUrl) }
                .onFailure { _sideEffect.send(LoginSideEffect.ShowToast("약관을 불러오지 못했어요")) }
        }
    }

    fun onViewPrivacyTermClick() {
        viewModelScope.launch {
            policyRepository.getPolicies()
                .onSuccess { openTermsUrl(it.privacyUrl) }
                .onFailure { _sideEffect.send(LoginSideEffect.ShowToast("약관을 불러오지 못했어요")) }
        }
    }

    private suspend fun openTermsUrl(url: String) {
        if (url.isBlank()) {
            _sideEffect.send(LoginSideEffect.ShowToast("약관 페이지를 준비 중이에요"))
        } else {
            _sideEffect.send(LoginSideEffect.OpenUrl(url))
        }
    }

    fun onSignUpClick() {
        val currentState = _state.value
        val code = kakaoAuthCode ?: return
        if (!currentState.isSignUpEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            authRepository.loginWithKakao(
                code = code,
                termsAgreed = currentState.isServiceTermAgreed,
                privacyAgreed = currentState.isPrivacyTermAgreed,
            ).onSuccess { token ->
                tokenManager.saveTokens(token.accessToken, token.refreshToken)
                _state.update { it.copy(isLoading = false, isTermsBottomSheetVisible = false) }

                val hasNoDogs = petRepository.getDogs().getOrDefault(emptyList()).isEmpty()
                _sideEffect.send(if (hasNoDogs) LoginSideEffect.NavigateToSignup else LoginSideEffect.NavigateToHome)
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
                _sideEffect.send(LoginSideEffect.ShowToast("회원가입에 실패했어요"))
            }
        }
    }
}
