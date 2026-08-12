package com.office.meong.presentation.course.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.presentation.course.detail.model.toUiModel
import com.office.meong.presentation.course.detail.navigation.DetailCourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
    private val petRepository: PetRepository,
) : ViewModel() {
    private val courseId = savedStateHandle.toRoute<DetailCourse>().courseId

    private val _state = MutableStateFlow(DetailCourseState())
    val state: StateFlow<DetailCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<DetailCourseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchCourseDetail()
        fetchPetInfo()
    }

    fun retryCourseDetail() {
        fetchCourseDetail()
    }

    fun retryPetInfo() {
        fetchPetInfo()
    }

    fun selectPreviousDay() {
        _state.update { it.copy(selectedDayNumber = (it.selectedDayNumber - 1).coerceAtLeast(1)) }
    }

    fun selectNextDay() {
        val totalDays = _state.value.course.successData?.totalDays ?: return
        _state.update { it.copy(selectedDayNumber = (it.selectedDayNumber + 1).coerceAtMost(totalDays)) }
    }

    fun reorderCourseItems(dayNumber: Int, itemIds: List<Long>) {
        viewModelScope.launch {
            courseRepository.reorderCourseItems(courseId, dayNumber, itemIds)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course.toUiModel())) }
                }
                .onFailure {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("일정 순서 변경에 실패했어요"))
                }
        }
    }

    private fun fetchCourseDetail() {
        viewModelScope.launch {
            _state.update { it.copy(course = UiState.Loading) }

            courseRepository.getDetailCourse(courseId)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course.toUiModel()), selectedDayNumber = 1) }
                }
                .onFailure {
                    _state.update { it.copy(course = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("코스 정보를 불러오지 못했어요"))
                }
        }
    }

    private fun fetchPetInfo() {
        viewModelScope.launch {
            _state.update { it.copy(petInfo = UiState.Loading) }

            petRepository.getDogs()
                .onSuccess { pets ->
                    _state.update { currentState ->
                        currentState.copy(
                            petInfo = pets.firstOrNull()?.toInfo()
                                ?.let { UiState.Success(it) }
                                ?: UiState.Empty
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(petInfo = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("반려견 정보를 불러오지 못했어요"))
                }
        }
    }
}
