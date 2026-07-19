package com.office.meong.core.designsystem.component.chip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongColors
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongChip(
    chipText: String,
    modifier: Modifier = Modifier,
    isActivated: Boolean = false,
    isSelected: Boolean = false,
    chipType: ChipType = ChipType.SMALL,
    selectedBackgroundColor: Color = MeongTheme.colors.gray600,
    selectedContentColor: Color = MeongTheme.colors.white,
) {
    val chipStyle = chipType.toMeongChipStyle(MeongTheme.colors)
    val currentBgColor = when {
        isSelected -> selectedBackgroundColor
        isActivated -> MeongTheme.colors.primaryBg
        else -> chipStyle.backgroundColor
    }
    val currentTextColor = when {
        isSelected -> selectedContentColor
        isActivated -> MeongTheme.colors.primary
        else -> chipStyle.contentColor
    }

    Box(
        modifier = modifier
            .styleable {
                shape(chipStyle.shape)
                background(currentBgColor)
                contentPadding(
                    horizontal = chipStyle.horizontalPadding,
                    vertical = chipStyle.verticalPadding,
                )
                contentColor(currentTextColor)
            }
    ) {
        Text(
            text = chipText,
            style = MeongTheme.typography.label.label12Sb,
        )
    }
}

private data class MeongChipStyle(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val contentColor: Color,
    val backgroundColor: Color,
    val shape: Shape,
)

private fun ChipType.toMeongChipStyle(colors: MeongColors): MeongChipStyle = when (this) {
    ChipType.LARGE -> MeongChipStyle(
        horizontalPadding = 12.dp,
        verticalPadding = 8.dp,
        contentColor = colors.gray700,
        backgroundColor = colors.gray50,
        shape = RoundedCornerShape(999.dp),
    )
    ChipType.SMALL -> MeongChipStyle(
        horizontalPadding = 6.dp,
        verticalPadding = 3.dp,
        contentColor = colors.gray600,
        backgroundColor = colors.gray100,
        shape = RoundedCornerShape(4.dp),
    )
}

@Preview
@Composable
private fun MeongChipPreview() {
    MeongTheme {
        MeongChip(
            chipText = "테스트",
            chipType = ChipType.SMALL,
            isSelected = true,
        )
    }
}
