package com.office.meong.presentation.course.detail

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.data.course.model.CourseDetail

@Immutable
data class DetailCourseState(
    val course: UiState<CourseDetail> = UiState.Loading,
    val selectedDayNumber: Int = 1,
)

sealed interface DetailCourseSideEffect {
    data class ShowToast(val message: String) : DetailCourseSideEffect
}
