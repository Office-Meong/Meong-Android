package com.office.meong.presentation.mypage

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.data.policy.model.PolicyModel
import com.office.meong.data.user.model.UserInfoModel

@Immutable
data class MyPageState(
    val userInfo: UiState<UserInfoModel> = UiState.Loading,
    val petInfo: UiState<PetInfo> = UiState.Loading,
    val policy: UiState<PolicyModel> = UiState.Loading,
    val isWithdrawing: Boolean = false,
)

sealed interface MyPageSideEffect {
    data class ShowSnackBar(val message: String) : MyPageSideEffect
    data object NavigateToLogin : MyPageSideEffect
}
