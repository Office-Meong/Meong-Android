package com.office.meong.presentation.explore.detail

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.presentation.explore.detail.model.ExploreDetailUiModel
import com.office.meong.presentation.explore.detail.model.ExploreWalkCourseUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ExploreDetailState(
    val place: UiState<ExploreDetailUiModel> = UiState.Loading,
    val walkCourses: UiState<ImmutableList<ExploreWalkCourseUiModel>> = UiState.Loading,
)

sealed interface ExploreDetailSideEffect {
    data class ShowSnackBar(val message: String) : ExploreDetailSideEffect
}
