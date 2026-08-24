package com.office.meong.core.designsystem.component.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MeongTopbar(
    modifier: Modifier = Modifier,
    title: String? = null,
    isBackVisible: Boolean = true,
    onBackClick: () -> Unit = {},
    actionType: TopbarAction? = null,
    onActionClick: (TopbarAction) -> Unit = {},
    onActionPositioned: (LayoutCoordinates) -> Unit = {},
    isStrokeVisible: Boolean = false,
    containerColor: Color = MeongTheme.colors.white
) {
    val topbarStrokeColor = MeongTheme.colors.gray100

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isStrokeVisible) {
                    Modifier.drawBehind {
                        drawLine(
                            color = topbarStrokeColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }
                } else {
                    Modifier
                }
            )
            .styleable {
                background(containerColor)
                contentPadding(vertical = 12.dp, horizontal = 20.dp)
            }
    ) {
        if (isBackVisible) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_left),
                contentDescription = null,
                tint = MeongTheme.colors.gray900,
                modifier = Modifier
                    .noRippleClickable(
                        onClick = onBackClick
                    )
            )
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (title != null) {
            Text(
                text = title,
                style = MeongTheme.typography.title.title16Sb,
                color = MeongTheme.colors.gray900
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (actionType != null) {
            Icon(
                imageVector = ImageVector.vectorResource(actionType.iconRes),
                contentDescription = null,
                tint = MeongTheme.colors.gray900,
                modifier = Modifier
                    .onGloballyPositioned(onActionPositioned)
                    .noRippleClickable(onClick = {
                        onActionClick(actionType)
                    })
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MeongTopbarPreview() {
    MeongTheme {
        MeongTopbar(
            title = "코스 상세",
            isBackVisible = true,
            actionType = TopbarAction.MORE
        )
    }
}
