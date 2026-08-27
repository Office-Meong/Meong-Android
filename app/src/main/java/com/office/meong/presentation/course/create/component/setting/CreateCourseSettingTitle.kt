package com.office.meong.presentation.course.create.component.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.course.create.component.setting.CreateCourseTooltipDefaults as Defaults

@Composable
fun CreateCourseSettingTitle(
    title: String,
    modifier: Modifier = Modifier,
    hasInfoIcon: Boolean = false,
    tooltipMessage: String? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = title,
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
        )

        if (hasInfoIcon && tooltipMessage != null) {
            InfoIconWithTooltip(message = tooltipMessage)
        }
    }
}

@Composable
private fun InfoIconWithTooltip(message: String) {
    var showTooltip by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val arrowOffsetPx = with(density) { Defaults.ArrowOffsetFromLeft.roundToPx() }
    val gapPx = with(density) { Defaults.AnchorGap.roundToPx() }

    val positionProvider = remember(arrowOffsetPx, gapPx) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val x = anchorBounds.left + anchorBounds.width / 2 - arrowOffsetPx
                val y = anchorBounds.top - popupContentSize.height - gapPx
                return IntOffset(
                    x = x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0)),
                    y = y.coerceAtLeast(0),
                )
            }
        }
    }

    Box {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_info),
            contentDescription = "업무 시간 안내",
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable { showTooltip = !showTooltip },
            tint = MeongTheme.colors.gray700,
        )

        if (showTooltip) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { showTooltip = false },
                properties = PopupProperties(focusable = false),
            ) {
                TooltipBubble(
                    message = message,
                    onDismiss = { showTooltip = false },
                )
            }
        }
    }
}

@Composable
private fun TooltipBubble(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MeongTheme.colors.gray700

    Box(
        modifier = modifier
            .width(Defaults.BodyWidth)
            .height(Defaults.BodyHeight + Defaults.ArrowHeight)
            .drawBehind {
                val arrowHeightPx = Defaults.ArrowHeight.toPx()
                val arrowWidthPx = Defaults.ArrowWidth.toPx()
                val cornerRadiusPx = Defaults.CornerRadius.toPx()
                val arrowCenterX = Defaults.ArrowOffsetFromLeft.toPx()
                val bodyHeight = size.height - arrowHeightPx

                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = bodyHeight,
                            cornerRadius = CornerRadius(cornerRadiusPx),
                        )
                    )
                    moveTo((arrowCenterX - arrowWidthPx / 2).coerceAtLeast(cornerRadiusPx), bodyHeight)
                    lineTo(arrowCenterX, size.height)
                    lineTo((arrowCenterX + arrowWidthPx / 2).coerceAtMost(size.width - cornerRadiusPx), bodyHeight)
                    close()
                }
                drawPath(path, color = backgroundColor)
            }
            .padding(
                end = Defaults.HorizontalPadding,
                bottom = Defaults.ArrowHeight,
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = message,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.white,
                maxLines = 1,
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                    ),
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "툴팁 닫기",
                modifier = Modifier
                    .size(16.dp)
                    .noRippleClickable(onDismiss),
                tint = MeongTheme.colors.white,
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

@Preview
@Composable
private fun CreateCourseSettingTitlePreview() {
    MeongTheme {
        CreateCourseSettingTitle(
            title = "업무 시간",
            hasInfoIcon = true,
            tooltipMessage = "하루 중 업무에 집중하는 시간대를 알려주세요",
        )
    }
}
