package com.office.meong.core.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

enum class MeongPillButtonStyle {
    Default,
    Primary,
    PrimaryStrong,
}

@Composable
fun MeongPillButton(
    text: String,
    modifier: Modifier = Modifier,
    style: MeongPillButtonStyle = MeongPillButtonStyle.Default,
    horizontalPadding: Int = 16,
    verticalPadding: Int = 10,
    onClick: () -> Unit = {},
    @DrawableRes prefixIcon: Int? = null,
    @DrawableRes suffixIcon: Int? = null,
) {
    val backgroundColor = when (style) {
        MeongPillButtonStyle.Default -> MeongTheme.colors.white
        MeongPillButtonStyle.Primary -> MeongTheme.colors.primaryBg
        MeongPillButtonStyle.PrimaryStrong -> MeongTheme.colors.primaryLight
    }
    val contentColor = when (style) {
        MeongPillButtonStyle.Default -> MeongTheme.colors.gray900
        MeongPillButtonStyle.Primary, MeongPillButtonStyle.PrimaryStrong -> MeongTheme.colors.primary
    }

    Row(
        modifier = modifier
            .styleable {
                background(color = backgroundColor)
                contentColor(value = contentColor)
                shape(RoundedCornerShape(999.dp))
                contentPadding(horizontal = horizontalPadding.dp, vertical = verticalPadding.dp)
            }
            .noRippleClickable(
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefixIcon != null) {
            Icon(
                imageVector = ImageVector.vectorResource(prefixIcon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = text,
            style = MeongTheme.typography.label.label14Sb
        )

        if (suffixIcon != null) {
            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = ImageVector.vectorResource(suffixIcon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun MeongPillButtonPreview() {
    MeongTheme {
        MeongPillButton(
            text = "text",
            style = MeongPillButtonStyle.Primary,
            prefixIcon = R.drawable.ic_plus,
            suffixIcon = R.drawable.ic_chevron_right
        )
    }
}
