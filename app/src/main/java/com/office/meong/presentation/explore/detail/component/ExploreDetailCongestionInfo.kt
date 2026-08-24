package com.office.meong.presentation.explore.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.theme.MeongTheme

private object ExploreDetailTooltipDefaults {
    val BodyWidth = 281.dp
    val BodyHeight = 40.dp
    val ArrowOffsetFromLeft = 30.dp
    val ArrowHeight = 12.dp
    val ArrowWidth = 20.dp
    val CornerRadius = 10.dp
    val AnchorGap = 2.dp
    val HorizontalPadding = 12.dp
}

@Composable
fun ExploreDetailCongestionInfo(
    congestionLevel: String,
    tooltipText: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "예측 혼잡도",
                style = MeongTheme.typography.label.label16Sb,
                color = MeongTheme.colors.gray700
            )

            Spacer(modifier = Modifier.width(4.dp))

            InfoIconWithTooltip(message = tooltipText)
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "방문 전 현장 상황을 확인해보세요",
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray500
        )
        Spacer(modifier = Modifier.height(12.dp))

        MeongChip(
            chipText = congestionLevel,
            chipType = ChipType.SMALL
        )
    }
}

@Composable
private fun InfoIconWithTooltip(message: String) {
    var showTooltip by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val arrowOffsetPx = with(density) { ExploreDetailTooltipDefaults.ArrowOffsetFromLeft.roundToPx() }
    val gapPx = with(density) { ExploreDetailTooltipDefaults.AnchorGap.roundToPx() }

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
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_info),
            contentDescription = "혼잡도 정보",
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable { showTooltip = !showTooltip },
            tint = MeongTheme.colors.gray500
        )

        if (showTooltip) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { showTooltip = false },
                properties = PopupProperties(focusable = true, dismissOnBackPress = false),
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
            .width(ExploreDetailTooltipDefaults.BodyWidth)
            .height(ExploreDetailTooltipDefaults.BodyHeight + ExploreDetailTooltipDefaults.ArrowHeight)
            .drawBehind {
                val arrowHeightPx = ExploreDetailTooltipDefaults.ArrowHeight.toPx()
                val arrowWidthPx = ExploreDetailTooltipDefaults.ArrowWidth.toPx()
                val cornerRadiusPx = ExploreDetailTooltipDefaults.CornerRadius.toPx()
                val arrowCenterX = ExploreDetailTooltipDefaults.ArrowOffsetFromLeft.toPx()
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
                end = ExploreDetailTooltipDefaults.HorizontalPadding,
                bottom = ExploreDetailTooltipDefaults.ArrowHeight,
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
                modifier = Modifier.padding(start = 10.dp),
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
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

@Preview(showBackground = true)
@Composable
private fun ExploreDetailCongestionInfoPreview() {
    MeongTheme {
        ExploreDetailCongestionInfo(
            congestionLevel = "보통",
            tooltipText = "한국관광공사 정보를 바탕으로 하루 한 번 초기화돼요",
            modifier = Modifier.padding(20.dp)
        )
    }
}
