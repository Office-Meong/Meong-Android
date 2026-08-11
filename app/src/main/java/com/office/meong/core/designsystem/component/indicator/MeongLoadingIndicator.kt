package com.office.meong.core.designsystem.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongLoadingIndicator(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MeongTheme.colors.white,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .noRippleClickable { },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = MeongTheme.colors.primary
        )
    }
}
