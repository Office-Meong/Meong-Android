package com.office.meong.presentation.auth.model

sealed interface LoginSideEffect {
    data object NavigateToHome : LoginSideEffect
    data object NavigateToSignup : LoginSideEffect
    data class ShowToast(val message: String) : LoginSideEffect
    data class OpenUrl(val url: String) : LoginSideEffect
}
