package com.office.meong.presentation.mypage.withdraw

import androidx.compose.runtime.Immutable

@Immutable
data class WithdrawState(
    val isAgreed: Boolean = false,
    val isWithdrawing: Boolean = false,
) {
    val isWithdrawEnabled: Boolean
        get() = isAgreed && !isWithdrawing
}

sealed interface WithdrawSideEffect {
    data object NavigateToLogin : WithdrawSideEffect
    data class ShowSnackBar(val message: String) : WithdrawSideEffect
}
