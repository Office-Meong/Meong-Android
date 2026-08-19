package com.office.meong.presentation.course.result

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.presentation.course.result.model.ResultCourseUiModel
import com.office.meong.presentation.course.model.ScheduleUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ResultCourseState(
    val course: UiState<ResultCourseUiModel> = UiState.Loading,
    val selectedDayNumber: Int = 1,
    val accommodationAlternatives: UiState<ImmutableList<ScheduleUiModel>> = UiState.Loading,
)

sealed interface ResultCourseSideEffect {
    data class ShowToast(val message: String) : ResultCourseSideEffect
}
