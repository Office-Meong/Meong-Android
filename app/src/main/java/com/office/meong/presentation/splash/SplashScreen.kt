package com.office.meong.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.designsystem.theme.MeongTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun SplashRoute(
    navigateToHome: () -> Unit = {},
    navigateToSignup: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    viewModel: SplashViewModel = hiltViewModel()
) {
    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is SplashSideEffect.NavigateToHome -> navigateToHome()
            is SplashSideEffect.NavigateToSignup -> navigateToSignup()
            is SplashSideEffect.NavigateToLogin -> navigateToLogin()
        }
    }

    SplashScreen()
}
@Composable
private fun SplashScreen() {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = defaultShimmerTheme.copy(

            blendMode = BlendMode.SrcOver,
            rotation = 0f,
            shaderColors = listOf(
                Color.White.copy(alpha = 0f),
                Color.White.copy(alpha = 0.85f),
                Color.White.copy(alpha = 0f),
            ),
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_splash_logo),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.shimmer(shimmer)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MeongTheme {
        SplashScreen()
    }
}
