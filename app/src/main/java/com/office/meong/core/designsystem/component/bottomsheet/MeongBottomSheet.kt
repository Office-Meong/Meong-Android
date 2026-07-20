package com.office.meong.core.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.disableUpWardEvent
import com.office.meong.core.designsystem.theme.MeongTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeongBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor: Color = MeongTheme.colors.white,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = containerColor,
        dragHandle = null,
        modifier = modifier
    ) {
        // 시트 전체(핸들 포함)에서 위 방향 드래그를 소비해 시트가 위로 늘어나는 현상 차단.
        // 자식(휠 피커 등)이 먼저 소비한 이벤트는 건드리지 않으므로 내부 스크롤은 정상 동작한다.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .disableUpWardEvent(),
        ) {
            dragHandle?.invoke()
            content()
        }
    }
}

@Composable
fun MeongDragHandle(
    modifier: Modifier = Modifier,
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 16.dp,
    color: Color = MeongTheme.colors.gray400,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding, bottom = bottomPadding),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(width = 32.dp, height = 4.dp)
                .background(color = color, shape = CircleShape),
        )
    }
}
