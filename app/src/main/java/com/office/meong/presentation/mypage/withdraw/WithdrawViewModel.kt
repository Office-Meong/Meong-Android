package com.office.meong.presentation.mypage.withdraw

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.data.user.repository.UserRepository
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
class WithdrawViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _state = MutableStateFlow(WithdrawState())
    val state: StateFlow<WithdrawState> = _state.asStateFlow()

    private val _sideEffect = Channel<WithdrawSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onAgreementChange(isAgreed: Boolean) {
        _state.update { it.copy(isAgreed = isAgreed) }
    }

    fun onWithdrawConfirm() {
        if (!_state.value.isWithdrawEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isWithdrawing = true) }

            userRepository.deleteUser()
                .onSuccess {
                    tokenManager.clearTokens()
                    _state.update { it.copy(isWithdrawing = false) }
                    _sideEffect.send(WithdrawSideEffect.NavigateToLogin)
                }
                .onFailure {
                    _state.update { it.copy(isWithdrawing = false) }
                    _sideEffect.send(WithdrawSideEffect.ShowSnackBar("회원 탈퇴에 실패했어요"))
                }
        }
    }
}
