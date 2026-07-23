package com.office.meong.core.common.extension

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import kotlin.math.abs

fun Modifier.styleableBackground(
    color: Color,
    shape: Shape = RectangleShape,
    padding: Dp = 0.dp
): Modifier = this.styleable {
    shape(shape)
    background(color)
    if (padding != 0.dp) contentPadding(padding)
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

fun Modifier.forcePixelToDp(painter: Painter): Modifier {
    return this.size(
        width = painter.intrinsicSize.width.dp,
        height = painter.intrinsicSize.height.dp
    )
}

fun Modifier.systemBarColor(color: Color): Modifier = composed {
    val activity = LocalActivity.current

    val isDarkIcons = color.luminance() > 0.5f

    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LaunchedEffect(activity, isDarkIcons) {
        val window = activity?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)

        controller.isAppearanceLightStatusBars = isDarkIcons
        controller.isAppearanceLightNavigationBars = isDarkIcons
    }

    this.drawBehind {
        val statusBarHeightPx = with(density) { statusBarHeight.toPx() }
        val navBarHeightPx = with(density) { navBarHeight.toPx() }

        drawRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(size.width, statusBarHeightPx)
        )

        drawRect(
            color = color,
            topLeft = Offset(0f, size.height - navBarHeightPx),
            size = Size(size.width, navBarHeightPx)
        )
    }
}

fun Modifier.statusBarColor(backgroundColor: Color): Modifier = composed {
    val activity = LocalActivity.current
    val isDarkIcons = backgroundColor.luminance() > 0.5f
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LaunchedEffect(activity, isDarkIcons) {
        val window = activity?.window ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.isAppearanceLightStatusBars = isDarkIcons
    }

    this.drawBehind {
        drawRect(
            color = backgroundColor,
            topLeft = Offset.Zero,
            size = Size(size.width, statusBarHeight.toPx())
        )
    }
}

// LazyColumn 같이 스크롤 후 더 이상 스크롤할 곳이 없을 때 바텀시트 움직임 막기
fun Modifier.disableNestedScroll(): Modifier = composed {
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return Offset.Zero
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                return available
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return available
            }
        }
    }
    this.nestedScroll(connection)
}

/**
 * 바텀시트 내부에서 위로 당기는(Upward) 드래그, 스크롤 이벤트만 가로채서 소비하는 Modifier
 *
 * touch slop을 넘기 전에는 소비하지 않는다. slop 판정 전에 소비해버리면 자식 스크롤(휠 피커 등)이
 * 제스처를 빼앗긴 것으로 판단하고 드래그 판정을 취소하기 때문. 자식이 먼저 소비한 이벤트도 건드리지 않는다.
 */
fun Modifier.disableUpWardEvent(): Modifier = this.pointerInput(Unit) {
    val touchSlop = viewConfiguration.touchSlop

    awaitPointerEventScope {
        var accumulatedDy = 0f
        var isPastSlop = false

        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Main)

            event.changes.forEach { change ->
                if (change.changedToDownIgnoreConsumed()) {
                    accumulatedDy = 0f
                    isPastSlop = false
                }

                if (change.isConsumed || !change.pressed) return@forEach

                val dy = change.position.y - change.previousPosition.y

                if (!isPastSlop) {
                    accumulatedDy += dy
                    isPastSlop = abs(accumulatedDy) > touchSlop
                }

                // 손가락이 위로 향할 때만 차단
                if (isPastSlop && dy < 0) {
                    change.consume()
                }
            }
        }
    }
}
