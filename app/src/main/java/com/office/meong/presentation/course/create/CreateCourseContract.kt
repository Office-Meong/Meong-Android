package com.office.meong.presentation.course.create

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.course.WorkFocusLevel
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.region.Region
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Immutable
data class CreateCourseState(
    val petInfo: UiState<PetInfo> = UiState.Loading,
    val selectedRegion: Region? = null,
    val selectedAccommodationType: String? = null,
    val selectedStartDate: LocalDate? = null,
    val selectedEndDate: LocalDate? = null,
    val selectedStartWorkTime: LocalTime? = null,
    val selectedEndWorkTime: LocalTime? = null,
    val selectedWorkFocusLevel: WorkFocusLevel? = null,
    val isSubmitting: Boolean = false,
) {
    val isSubmittable: Boolean
        get() = selectedRegion != null &&
            selectedStartDate != null &&
            selectedEndDate != null &&
            selectedStartWorkTime != null &&
            selectedEndWorkTime != null &&
            selectedWorkFocusLevel != null
}

sealed interface CreateCourseSideEffect {
    data class ShowToast(val message: String) : CreateCourseSideEffect
    data class NavigateToResult(val courseId: Long) : CreateCourseSideEffect
}
