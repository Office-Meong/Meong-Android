package com.office.meong.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.localstorage.token.TokenManager
import com.office.meong.data.auth.repository.AuthRepository
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.policy.repository.PolicyRepository
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
class MyPageViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {
    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    private val _sideEffect = Channel<MyPageSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchPolicy()
        fetchUserInfo()
        fetchPetInfo()
    }

    private fun fetchPolicy() {
        viewModelScope.launch {
            _state.update { it.copy(policy = UiState.Loading) }

            policyRepository.getPolicies()
                .onSuccess { policy ->
                    _state.update { it.copy(policy = UiState.Success(policy)) }
                }
                .onFailure {
                    _state.update { it.copy(policy = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            userRepository.getUserInfo()
                .onSuccess { user ->
                    _state.update { it.copy(userInfo = UiState.Success(user)) }
                }
                .onFailure {
                    _state.update { it.copy(userInfo = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    private fun fetchPetInfo() {
        viewModelScope.launch {
            petRepository.getDogs()
                .onSuccess { dogs ->
                    val pet = dogs.firstOrNull()?.toInfo()
                    _state.update {
                        it.copy(petInfo = pet?.let { info -> UiState.Success(info) } ?: UiState.Empty)
                    }
                }
                .onFailure {
                    _state.update { it.copy(petInfo = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            authRepository.logout()
                .onSuccess {
                    tokenManager.clearTokens()
                    _sideEffect.send(MyPageSideEffect.NavigateToLogin)
                }
                .onFailure {
                    _sideEffect.send(MyPageSideEffect.ShowSnackBar("로그아웃에 실패했어요"))
                }
        }
    }

    fun onWithdrawClick() {
        if (_state.value.isWithdrawing) return

        viewModelScope.launch {
            _state.update { it.copy(isWithdrawing = true) }

            userRepository.deleteUser()
                .onSuccess {
                    tokenManager.clearTokens()
                    _state.update { it.copy(isWithdrawing = false) }
                    _sideEffect.send(MyPageSideEffect.NavigateToLogin)
                }
                .onFailure {
                    _state.update { it.copy(isWithdrawing = false) }
                    _sideEffect.send(MyPageSideEffect.ShowSnackBar("회원 탈퇴에 실패했어요"))
                }
        }
    }
}
