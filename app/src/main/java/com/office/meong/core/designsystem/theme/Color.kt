package com.office.meong.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Primary
val Primary = Color(0xFF7153F5)
val PrimaryLight = Color(0xFFE7E1FF)
val PrimaryBg = Color(0xFFF3F0FF)

// Secondary
val Secondary = Color(0xFFF7802B)
val SecondaryLight = Color(0xFFFFE7D9)
val SecondaryBg = Color(0xFFFFF3EC)

// Red
val Red = Color(0xFFFF3C56)

// Grayscale
val White = Color(0xFFFFFFFF)
val Gray50 = Color(0xFFF4F5F6)
val Gray100 = Color(0xFFE6E8EA)
val Gray200 = Color(0xFFCDD1D5)
val Gray300 = Color(0xFFB1B8BE)
val Gray400 = Color(0xFF8A949E)
val Gray500 = Color(0xFF6D7882)
val Gray600 = Color(0xFF58616A)
val Gray700 = Color(0xFF464C53)
val Gray800 = Color(0xFF33363D)
val Gray900 = Color(0xFF1E2124)
val Black = Color(0xFF000000)

// Alpha Colors (Black base)
val BlackAlpha70 = Color(0xB3000000) // 70% Opacity
val BlackAlpha10 = Color(0x1A000000) // 10% Opacity

@Immutable
data class MeongColors(
    val primary: Color = Primary,
    val primaryLight: Color = PrimaryLight,
    val primaryBg: Color = PrimaryBg,
    val secondary: Color = Secondary,
    val secondaryLight: Color = SecondaryLight,
    val secondaryBg: Color = SecondaryBg,
    val red: Color = Red,

    val white: Color = White,
    val gray50: Color = Gray50,
    val gray100: Color = Gray100,
    val gray200: Color = Gray200,
    val gray300: Color = Gray300,
    val gray400: Color = Gray400,
    val gray500: Color = Gray500,
    val gray600: Color = Gray600,
    val gray700: Color = Gray700,
    val gray800: Color = Gray800,
    val gray900: Color = Gray900,
    val black: Color = Black,

    val blackAlpha70: Color = BlackAlpha70,
    val blackAlpha10: Color = BlackAlpha10
)

val defaultMeongColors = MeongColors()
