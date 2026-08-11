package com.office.meong.presentation.course.detail

import androidx.compose.runtime.Immutable

@Immutable
data class DetailCourseState(
)

sealed interface DetailCourseSideEffect {
    data class ShowToast(val message: String) : DetailCourseSideEffect
}
