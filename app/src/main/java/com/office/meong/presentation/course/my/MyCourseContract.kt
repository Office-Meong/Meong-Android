package com.office.meong.presentation.course.my

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.presentation.course.my.model.MyCourseSummaryUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class MyCourseState(
    val myCoursesSummaries: UiState<ImmutableList<MyCourseSummaryUiModel>> = UiState.Loading,
    val isRefreshing: Boolean = false,
)

sealed interface MyCourseSideEffect {
    data class ShowSnackBar(val message: String) : MyCourseSideEffect
}
