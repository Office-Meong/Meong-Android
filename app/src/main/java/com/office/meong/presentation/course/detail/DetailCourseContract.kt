package com.office.meong.presentation.course.detail

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.presentation.course.detail.model.DetailCourseUiModel

@Immutable
data class DetailCourseState(
    val course: UiState<DetailCourseUiModel> = UiState.Loading,
    val petInfo: UiState<PetInfo> = UiState.Loading,
    val selectedDayNumber: Int = 1,
)

sealed interface DetailCourseSideEffect {
    data class ShowToast(val message: String) : DetailCourseSideEffect
    data object NavigateUp : DetailCourseSideEffect
}
