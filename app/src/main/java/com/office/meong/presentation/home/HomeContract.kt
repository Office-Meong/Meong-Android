package com.office.meong.presentation.home

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.presentation.home.model.HomeCourseSummaryUiModel
import com.office.meong.presentation.home.model.HomePetInfoUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class HomeState(
    val petInfo: UiState<HomePetInfoUiModel> = UiState.Loading,
    val homeCourseSummaries: UiState<ImmutableList<HomeCourseSummaryUiModel>> = UiState.Loading,
)

sealed interface HomeSideEffect {
    data class ShowSnackBar(val message: String) : HomeSideEffect
}
