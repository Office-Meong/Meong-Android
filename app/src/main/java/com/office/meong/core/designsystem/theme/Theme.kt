package com.office.meong.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val localMeongColors = staticCompositionLocalOf { defaultMeongColors }

val localMeongTypography = staticCompositionLocalOf { defaultMeongTypography }

private val LightColorScheme = lightColorScheme(
    primary = Black,
    background = White,
    surface = White,
    onPrimary = White,
    onBackground = Black,
    onSurface = Black
)

object MeongTheme {
    val colors: MeongColors
        @Composable
        @ReadOnlyComposable
        get() = localMeongColors.current

    val typography: MeongTypography
        @Composable
        @ReadOnlyComposable
        get() = localMeongTypography.current
}

@Composable
fun ProvideMeongColorsAndTypography(
    colors: MeongColors,
    typography: MeongTypography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        localMeongColors provides colors,
        localMeongTypography provides typography,
        content = content
    )
}


@Composable
fun MeongTheme(
    content: @Composable () -> Unit
) {
    ProvideMeongColorsAndTypography(
        colors = defaultMeongColors,
        typography = defaultMeongTypography
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}
