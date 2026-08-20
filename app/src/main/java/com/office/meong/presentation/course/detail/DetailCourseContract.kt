package com.office.meong.presentation.course.detail

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.presentation.course.detail.model.DetailCourseUiModel
import com.office.meong.presentation.course.model.ScheduleUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableSet

@Immutable
data class DetailCourseState(
    val course: UiState<DetailCourseUiModel> = UiState.Loading,
    val petInfo: UiState<PetInfo> = UiState.Loading,
    val selectedDayNumber: Int = 1,
    val accommodationAlternatives: UiState<ImmutableList<ScheduleUiModel>> = UiState.Loading,
    val favoritePlaces: UiState<ImmutableList<ScheduleUiModel>> = UiState.Loading,
) {
    val favoritePlaceIds: ImmutableSet<Long>
        get() = (favoritePlaces as? UiState.Success)?.data?.mapNotNull { it.placeId }?.toImmutableSet()
            ?: persistentSetOf()
}

sealed interface DetailCourseSideEffect {
    data class ShowToast(val message: String) : DetailCourseSideEffect
    data object NavigateUp : DetailCourseSideEffect
}
