package com.office.meong.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.office.meong.R
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun SplashRoute() {
    SplashScreen()
}
@Composable
private fun SplashScreen(

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white)
        , contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_splash_logo),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MeongTheme {
        SplashRoute()
    }

}