package com.office.meong.presentation.course.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.toScheduleUiModel
import com.office.meong.presentation.course.result.model.toUiModel
import com.office.meong.presentation.course.result.navigation.ResultCourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
) : ViewModel() {
    private val courseId = savedStateHandle.toRoute<ResultCourse>().courseId

    private val _state = MutableStateFlow(ResultCourseState())
    val state: StateFlow<ResultCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<ResultCourseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchCourseDetail()
    }

    fun retryCourseDetail() {
        fetchCourseDetail()
    }

    fun selectPreviousDay() {
        _state.update { it.copy(selectedDayNumber = (it.selectedDayNumber - 1).coerceAtLeast(1)) }
    }

    fun selectNextDay() {
        val totalDays = _state.value.course.successData?.totalDays ?: return
        _state.update { it.copy(selectedDayNumber = (it.selectedDayNumber + 1).coerceAtMost(totalDays)) }
    }

    fun fetchAccommodationAlternatives() {
        val itemId = accommodationItemId() ?: return

        viewModelScope.launch {
            _state.update { it.copy(accommodationAlternatives = UiState.Loading) }

            courseRepository.getCourseItemAlternatives(courseId, itemId)
                .onSuccess { alternatives ->
                    _state.update {
                        it.copy(
                            accommodationAlternatives = UiState.Success(
                                alternatives.map { place -> place.toScheduleUiModel() }.toImmutableList()
                            )
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(accommodationAlternatives = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun selectAccommodationAlternative(place: ScheduleUiModel) {
        val itemId = accommodationItemId() ?: return
        val placeId = place.placeId ?: return

        viewModelScope.launch {
            courseRepository.updateCourseItem(
                courseId = courseId,
                itemId = itemId,
                startTime = null,
                endTime = null,
                newPlaceId = placeId
            )
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course.toUiModel())) }
                }
                .onFailure {
                    _sideEffect.send(ResultCourseSideEffect.ShowToast("숙소 변경에 실패했어요"))
                }
        }
    }

    private fun accommodationItemId(): Long? =
        _state.value.course.successData?.accommodation?.id?.toLongOrNull()

    fun reorderCourseItems(dayNumber: Int, itemIds: List<Long>) {
        viewModelScope.launch {
            courseRepository.reorderCourseItems(courseId, dayNumber, itemIds)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course.toUiModel())) }
                }
                .onFailure {
                    _sideEffect.send(ResultCourseSideEffect.ShowToast("일정 순서 변경에 실패했어요"))
                }
        }
    }

    private fun fetchCourseDetail() {
        viewModelScope.launch {
            _state.update { it.copy(course = UiState.Loading) }

            courseRepository.getDetailCourse(courseId)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course.toUiModel())) }
                }
                .onFailure {
                    _state.update { it.copy(course = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(ResultCourseSideEffect.ShowToast("코스 정보를 불러오지 못했어요"))
                }
        }
    }
}
