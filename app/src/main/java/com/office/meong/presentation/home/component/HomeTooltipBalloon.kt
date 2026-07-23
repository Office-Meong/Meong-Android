package com.office.meong.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

val TooltipShape: Shape = object : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val sx = size.width / 212f
        val sy = size.height / 76f
        fun px(x: Float) = x * sx
        fun py(y: Float) = y * sy

        val bodyPath = Path().apply {
            addRoundRect(
                RoundRect(
                    left = px(3f), top = py(1f),
                    right = px(203f), bottom = py(77f),
                    radiusX = px(20f), radiusY = py(20f)
                )
            )
        }

        val tailPath = Path().apply {
            moveTo(px(212.078f), py(33.7682f))
            lineTo(px(202.64f), py(41.6332f))
            cubicTo(px(201.989f), py(42.176f), px(201f), py(41.7128f), px(201f), py(40.865f))
            lineTo(px(201f), py(25.135f))
            cubicTo(px(201f), py(24.2872f), px(201.989f), py(23.824f), px(202.64f), py(24.3668f))
            lineTo(px(212.078f), py(32.2318f))
            cubicTo(px(212.558f), py(32.6316f), px(212.558f), py(33.3684f), px(212.078f), py(33.7682f))
            close()
        }

        val merged = Path()
        merged.op(bodyPath, tailPath, PathOperation.Union)

        return Outline.Generic(merged)
    }
}

@Composable
fun HomeTooltipBalloon(
    text: String,
    modifier: Modifier = Modifier,
    emphasizeText: String? = null,
) {
    Box(
        modifier = modifier
            .size(width = 212.dp, height = 76.dp)
            .dropShadow(
                shape = TooltipShape,
                shadow = Shadow(
                    color = MeongTheme.colors.gray900,
                    offset = DpOffset(2.dp, 4.dp),
                    radius = 5.dp,
                    alpha = 0.1f
                )
            )
            .background(Color.White, TooltipShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    MeongTheme.typography.body.body14M.copy(
                        color = MeongTheme.colors.primary
                    ).toSpanStyle()
                ) {
                    append(emphasizeText ?: "")
                }

                append(text)
            },
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray900,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeTooltipBalloonPreview() {
    MeongTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MeongTheme.colors.white),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HomeTooltipBalloon(
                text = "와 함께할 ",
                emphasizeText = "오늘의",
            )
        }
    }
}
