package com.office.meong.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.data.policy.repository.PolicyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val policyRepository: PolicyRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MyPageState())
    val state: StateFlow<MyPageState> = _state.asStateFlow()

    init {
        fetchPolicy()
    }

    private fun fetchPolicy() {
        viewModelScope.launch {
            _state.update { it.copy(policy = UiState.Loading) }

            policyRepository.getPolicies()
                .onSuccess { policy ->
                    _state.update { currentState ->
                        currentState.copy(policy = UiState.Success(policy))
                    }
                }
                .onFailure {
                    _state.update { currentState ->
                        currentState.copy(policy = UiState.Failure(LoadErrorHandleAction.Retry))
                    }
                }
        }
    }
}