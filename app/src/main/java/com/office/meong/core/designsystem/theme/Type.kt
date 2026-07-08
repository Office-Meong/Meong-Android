package com.office.meong.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.office.meong.R

object MeongFont {
    val semiBold = FontFamily(Font(R.font.pretendard_semibold))
    val bold     = FontFamily(Font(R.font.pretendard_bold))
    val regular  = FontFamily(Font(R.font.pretendard_regular))
    val medium   = FontFamily(Font(R.font.pretendard_medium))
    val extraBold = FontFamily(Font(R.font.pretendard_extrabold))
}

private object TypographyDefaults {
    val DefaultLetterSpacing = (-0.02).em
    val PlatformStyle = PlatformTextStyle(includeFontPadding = false)
}

sealed interface TypographyTokens {
    /**
     * Label 텍스트 (SemiBold 600)
     */
    @Immutable
    data class Label(
        val label10Sb: TextStyle,
        val label12Sb: TextStyle,
        val label14Sb: TextStyle,
        val label16Sb: TextStyle,
    )

    /**
     * Title 텍스트 (SemiBold 600)
     */
    @Immutable
    data class Title(
        val title16Sb: TextStyle,
        val title20Sb: TextStyle,
    )

    /**
     * Body 텍스트 (Medium 500)
     */
    @Immutable
    data class Body(
        val body12M: TextStyle,
        val body14M: TextStyle,
        val body16M: TextStyle,
    )
}

@Immutable
data class MeongTypography(
    val label: TypographyTokens.Label,
    val title: TypographyTokens.Title,
    val body: TypographyTokens.Body,
)

val defaultMeongTypography = MeongTypography(
    label = TypographyTokens.Label(
        label10Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 10.sp,
            lineHeight    = 12.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        label12Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 12.sp,
            lineHeight    = 14.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        label14Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 14.sp,
            lineHeight    = 16.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        label16Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 16.sp,
            lineHeight    = 18.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
    ),
    title = TypographyTokens.Title(
        title16Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 16.sp,
            lineHeight    = 20.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        title20Sb = TextStyle(
            fontFamily    = MeongFont.semiBold,
            fontSize      = 20.sp,
            lineHeight    = 30.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
    ),
    body = TypographyTokens.Body(
        body12M = TextStyle(
            fontFamily    = MeongFont.medium,
            fontSize      = 12.sp,
            lineHeight    = 20.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        body14M = TextStyle(
            fontFamily    = MeongFont.medium,
            fontSize      = 14.sp,
            lineHeight    = 22.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
        body16M = TextStyle(
            fontFamily    = MeongFont.medium,
            fontSize      = 16.sp,
            lineHeight    = 24.sp,
            letterSpacing = TypographyDefaults.DefaultLetterSpacing,
            platformStyle = TypographyDefaults.PlatformStyle,
        ),
    )
)
