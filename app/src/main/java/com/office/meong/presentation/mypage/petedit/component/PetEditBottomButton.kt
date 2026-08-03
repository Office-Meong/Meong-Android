package com.office.meong.presentation.mypage.petedit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun PetEditBottomButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isEnabled) MeongTheme.colors.primary else MeongTheme.colors.gray200
    val textColor = if (isEnabled) Color.White else MeongTheme.colors.gray500

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .clickable(
                interactionSource = null,
                enabled = isEnabled,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "저장하기",
            style = MeongTheme.typography.label.label16Sb,
            color = textColor
        )
    }
}
