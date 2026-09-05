package com.office.meong.presentation.explore.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.openKakaoMap
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.explore.detail.component.ExploreDetailAccessibilityInfo
import com.office.meong.presentation.explore.detail.component.ExploreDetailActionRow
import com.office.meong.presentation.explore.detail.component.ExploreDetailCongestionInfo
import com.office.meong.presentation.explore.detail.component.ExploreDetailHeader
import com.office.meong.presentation.explore.detail.component.ExploreDetailOperationInfo
import com.office.meong.presentation.explore.detail.component.ExploreDetailPetCompanionInfo
import com.office.meong.presentation.explore.detail.component.ExploreDetailPetWorkIndex
import com.office.meong.presentation.explore.detail.component.ExploreDetailWalkCourseSection
import com.office.meong.presentation.explore.detail.model.ExploreDetailUiModel
import com.office.meong.presentation.explore.detail.model.ExploreWalkCourseUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun ExploreDetailRoute(
    onBackClick: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: ExploreDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ExploreDetailSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    when (val place = state.place) {
        is UiState.Loading -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                MeongTopbar(onBackClick = onBackClick)
                MeongLoadingIndicator(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth())
            }
        }

        is UiState.Failure -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                MeongTopbar(onBackClick = onBackClick)
                MeongLoadErrorView(
                    action = LoadErrorViewAction.Retry(onRetryClick = viewModel::retry),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        is UiState.Empty -> Unit

        is UiState.Success -> {
            ExploreDetailScreen(
                paddingValues = paddingValues,
                uiState = place.data,
                walkCourses = state.walkCourses.successData.orEmpty().toImmutableList(),
                onBackClick = onBackClick,
                onKakaoMapClick = { context.openKakaoMap(place.data.address.ifBlank { place.data.title }) },
                onFavoriteClick = viewModel::onFavoriteClick,
                onWalkCourseClick = { course ->
                    context.openKakaoMap(course.courseName, course.latitude, course.longitude)
                }
            )
        }
    }
}

@Composable
private fun ExploreDetailScreen(
    paddingValues: PaddingValues,
    uiState: ExploreDetailUiModel,
    walkCourses: ImmutableList<ExploreWalkCourseUiModel>,
    onBackClick: () -> Unit,
    onKakaoMapClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onWalkCourseClick: (ExploreWalkCourseUiModel) -> Unit,
) {
    var congestionTooltipVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = congestionTooltipVisible) {
        congestionTooltipVisible = false
        onBackClick()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.white)
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MeongTopbar(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ExploreDetailHeader(
                placeType = uiState.placeType,
                title = uiState.title,
                address = uiState.address,
                imageUrl = uiState.imageUrl
            )

            ExploreDetailActionRow(
                onKakaoMapClick = onKakaoMapClick,
                onFavoriteClick = onFavoriteClick,
                isFavorite = uiState.isFavorite
            )

            ExploreDetailPetWorkIndex(
                grade = uiState.grade
            )

            ExploreDetailPetCompanionInfo(
                isAllowed = uiState.isAllowed,
                condition = uiState.condition,
                allowedSpace = uiState.allowedSpace,
                notice = uiState.notice
            )

            ExploreDetailOperationInfo(
                todayHours = uiState.todayHours,
                weeklyHours = uiState.weeklyHours,
                closedDays = uiState.closedDays,
                parkingInfo = uiState.parkingInfo,
                phoneNumber = uiState.phoneNumber
            )

            ExploreDetailCongestionInfo(
                congestionLevel = uiState.congestionLevel,
                tooltipText = uiState.tooltipText,
                tooltipVisible = congestionTooltipVisible,
                onTooltipVisibleChange = { congestionTooltipVisible = it }
            )

            ExploreDetailAccessibilityInfo(
                accessibilityTags = uiState.accessibilityTags
            )

            ExploreDetailWalkCourseSection(
                courses = walkCourses,
                onCourseClick = onWalkCourseClick
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

}

@Preview
@Composable
private fun ExploreDetailScreenPreview() {
    MeongTheme {
        ExploreDetailScreen(
            paddingValues = PaddingValues(),
            uiState = ExploreDetailUiModel.Dummy,
            walkCourses = persistentListOf(),
            onBackClick = {},
            onKakaoMapClick = {},
            onFavoriteClick = {},
            onWalkCourseClick = {}
        )
    }
}
