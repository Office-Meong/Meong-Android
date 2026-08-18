package com.office.meong.presentation.mypage

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.data.policy.model.PolicyModel

@Immutable
data class MyPageState(
    val policy: UiState<PolicyModel> = UiState.Loading,
)