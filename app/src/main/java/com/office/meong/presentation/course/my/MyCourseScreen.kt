package com.office.meong.presentation.course.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.refresh.MeongPullToRefreshBox
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.core.trigger.LocalRefreshState
import com.office.meong.presentation.course.my.component.MyCourseItem
import com.office.meong.presentation.sharedcomponent.CourseEmptyContent

@Composable
fun MyCourseRoute(
    paddingValues: PaddingValues,
    navigateToDetailCourse: (Long) -> Unit = {},
    navigateToCreateCourse: () -> Unit = {},
    viewModel: MyCourseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val refreshState = LocalRefreshState.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    // 탭 재탭: 맨 위가 아니면 맨 위로, 맨 위면 새로고침.
    LaunchedEffect(Unit) {
        refreshState.events.collect {
            if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                viewModel.refresh(userInitiated = true)
            } else {
                listState.scrollToItem(0)
            }
        }
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is MyCourseSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    MyCourseScreen(
        paddingValues = paddingValues,
        state = state,
        listState = listState,
        onRetry = viewModel::retryMyCourses,
        onRefresh = { viewModel.refresh(userInitiated = true) },
        navigateToDetailCourse = navigateToDetailCourse,
        navigateToCreateCourse = navigateToCreateCourse,
    )
}

@Composable
private fun MyCourseScreen(
    paddingValues: PaddingValues,
    state: MyCourseState,
    listState: LazyListState,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    navigateToDetailCourse: (Long) -> Unit = {},
    navigateToCreateCourse: () -> Unit = {},
) {
    val summaries = state.myCoursesSummaries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.gray50)
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = "내 코스",
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "총 ${summaries.successData?.size ?: 0}개",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        MeongPullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (summaries) {
                is UiState.Loading -> {
                    MeongLoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is UiState.Failure -> {
                    MeongLoadErrorView(
                        action = LoadErrorViewAction.Retry(onRetryClick = onRetry),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Empty -> {
                    CourseEmptyContent(
                        onClickPillButton = navigateToCreateCourse,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is UiState.Success -> {
                    if (summaries.data.isEmpty()) {
                        CourseEmptyContent(
                            onClickPillButton = navigateToCreateCourse,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = summaries.data,
                                key = { it.id }
                            ) { course ->
                                MyCourseItem(
                                    location = course.region.label,
                                    tripPeriod = course.tripPeriod,
                                    title = course.name,
                                    grade = course.averageGrade,
                                    places = course.places,
                                    totalPlaceCount = course.totalPlaceCount,
                                    onClickCourseItem = { navigateToDetailCourse(course.id) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(30.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MyCourseScreenPreview() {
    MeongTheme {
        MyCourseScreen(
            paddingValues = PaddingValues(),
            state = MyCourseState(myCoursesSummaries = UiState.Loading),
            listState = rememberLazyListState(),
            onRetry = {},
            onRefresh = {},
        )
    }
}
