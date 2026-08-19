package com.office.meong.presentation.splash

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToLogin : SplashSideEffect
}
