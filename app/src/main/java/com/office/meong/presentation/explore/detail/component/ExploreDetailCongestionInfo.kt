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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
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
    val BodyWidth = 320.dp
    val BodyHeight = 40.dp
    val ArrowHeight = 12.dp
    val ArrowWidth = 20.dp
    val CornerRadius = 10.dp
    val AnchorGap = 2.dp
    val HorizontalPadding = 12.dp
    val ScreenEdgeMargin = 20.dp
}

@Composable
fun ExploreDetailCongestionInfo(
    congestionLevel: String,
    tooltipText: String,
    tooltipVisible: Boolean,
    onTooltipVisibleChange: (Boolean) -> Unit,
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

            InfoIconWithTooltip(
                message = tooltipText,
                visible = tooltipVisible,
                onVisibleChange = onTooltipVisibleChange,
            )
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
private fun InfoIconWithTooltip(
    message: String,
    visible: Boolean,
    onVisibleChange: (Boolean) -> Unit,
) {
    var anchorCenterXInWindow by remember { mutableFloatStateOf(0f) }
    var popupXInWindow by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current
    val arrowOffsetPx = remember(anchorCenterXInWindow, popupXInWindow, density) {
        val cornerRadiusPx = with(density) { ExploreDetailTooltipDefaults.CornerRadius.toPx() }
        val arrowHalfWidthPx = with(density) { (ExploreDetailTooltipDefaults.ArrowWidth / 2).toPx() }
        val bodyWidthPx = with(density) { ExploreDetailTooltipDefaults.BodyWidth.toPx() }
        (anchorCenterXInWindow - popupXInWindow)
            .coerceIn(cornerRadiusPx + arrowHalfWidthPx, bodyWidthPx - cornerRadiusPx - arrowHalfWidthPx)
    }

    val positionProvider = remember(density) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                val gapPx = with(density) { ExploreDetailTooltipDefaults.AnchorGap.roundToPx() }
                val edgeMarginPx = with(density) { ExploreDetailTooltipDefaults.ScreenEdgeMargin.roundToPx() }

                val anchorCenterX = anchorBounds.left + anchorBounds.width / 2
                val maxX = (windowSize.width - popupContentSize.width - edgeMarginPx).coerceAtLeast(edgeMarginPx)
                val x = (anchorCenterX - popupContentSize.width / 2).coerceIn(edgeMarginPx, maxX)
                val y = (anchorBounds.top - popupContentSize.height - gapPx).coerceAtLeast(0)

                popupXInWindow = x.toFloat()

                return IntOffset(x = x, y = y)
            }
        }
    }

    Box(
        modifier = Modifier.onGloballyPositioned { coordinates ->
            val position = coordinates.positionInWindow()
            anchorCenterXInWindow = position.x + coordinates.size.width / 2f
        }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_info),
            contentDescription = "혼잡도 정보",
            modifier = Modifier
                .size(16.dp)
                .noRippleClickable { onVisibleChange(!visible) },
            tint = MeongTheme.colors.gray500
        )

        if (visible) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { onVisibleChange(false) },
                properties = PopupProperties(focusable = false),
            ) {
                TooltipBubble(
                    message = message,
                    arrowOffsetPx = arrowOffsetPx,
                    onDismiss = { onVisibleChange(false) },
                )
            }
        }
    }
}

@Composable
private fun TooltipBubble(
    message: String,
    arrowOffsetPx: Float,
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
                    moveTo((arrowOffsetPx - arrowWidthPx / 2).coerceAtLeast(cornerRadiusPx), bodyHeight)
                    lineTo(arrowOffsetPx, size.height)
                    lineTo((arrowOffsetPx + arrowWidthPx / 2).coerceAtMost(size.width - cornerRadiusPx), bodyHeight)
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
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(start = 10.dp),
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = "툴팁 닫기",
                modifier = Modifier
                    .size(16.dp)
                    .noRippleClickable(onDismiss),
                tint = MeongTheme.colors.white,
            )
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
            tooltipVisible = false,
            onTooltipVisibleChange = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
