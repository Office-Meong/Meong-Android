package com.office.meong.core.designsystem.component.refresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.office.meong.core.designsystem.theme.MeongTheme

/**
 * 앱 공통 당겨서 새로고침 컨테이너.
 * 인디케이터는 흰 원 + primary 색 화살표이며, 당기는 만큼 따라 내려오도록
 * box 와 indicator 가 같은 [rememberPullToRefreshState] 를 공유한다.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeongPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        modifier = modifier,
        indicator = {
            PullToRefreshDefaults.Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                containerColor = MeongTheme.colors.white,
                color = MeongTheme.colors.primary,
            )
        },
        content = content,
    )
}
