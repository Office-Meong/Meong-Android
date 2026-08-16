package com.office.meong.presentation.course.result

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.presentation.course.result.model.ResultCourseUiModel

@Immutable
data class ResultCourseState(
    val course: UiState<ResultCourseUiModel> = UiState.Loading,
    val selectedDayNumber: Int = 1,
)

sealed interface ResultCourseSideEffect {
    data class ShowToast(val message: String) : ResultCourseSideEffect
}
