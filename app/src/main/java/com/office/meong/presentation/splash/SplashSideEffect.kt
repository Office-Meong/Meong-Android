package com.office.meong.presentation.splash

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToSignup : SplashSideEffect
    data class NavigateToLogin(val skipTermsBottomSheet: Boolean = false) : SplashSideEffect
}
