package com.office.meong.presentation.course.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.formatTripDuration
import com.office.meong.core.common.util.successData
import com.office.meong.core.model.course.WorkFocusLevel
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseCreateInput
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val petRepository: PetRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CreateCourseState())
    val state: StateFlow<CreateCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<CreateCourseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchPetInfo()
    }

    fun retryPetInfo() {
        fetchPetInfo()
    }

    fun selectRegion(region: Region) {
        _state.update {
            it.copy(selectedRegion = if (it.selectedRegion == region) null else region)
        }
    }

    fun selectAccommodationType(type: String) {
        _state.update {
            it.copy(
                selectedAccommodationType = if (it.selectedAccommodationType == type) null else type
            )
        }
    }

    fun selectDateRange(date: LocalDate) {
        _state.update { current ->
            when {
                current.selectedStartDate == null || current.selectedEndDate != null ->
                    current.copy(selectedStartDate = date, selectedEndDate = null)

                date >= current.selectedStartDate -> current.copy(selectedEndDate = date)
                else -> current.copy(selectedStartDate = date)
            }
        }
    }

    fun selectStartWorkTime(time: LocalTime) {
        _state.update { it.copy(selectedStartWorkTime = time) }
    }

    fun selectEndWorkTime(time: LocalTime) {
        _state.update { it.copy(selectedEndWorkTime = time) }
    }

    fun selectWorkFocusLevel(level: WorkFocusLevel) {
        _state.update { it.copy(selectedWorkFocusLevel = level) }
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
                    _state.update { currentState ->
                        currentState.copy(
                            petInfo = UiState.Failure(LoadErrorHandleAction.Retry)
                        )
                    }
                }
        }
    }

    fun createCourse() {
        val current = _state.value
        val dogId = current.petInfo.successData?.id
        val region = current.selectedRegion
        val startDate = current.selectedStartDate
        val endDate = current.selectedEndDate
        val startTime = current.selectedStartWorkTime
        val endTime = current.selectedEndWorkTime
        val workFocusLevel = current.selectedWorkFocusLevel

        if (dogId == null || region == null || startDate == null || endDate == null ||
            startTime == null || endTime == null || workFocusLevel == null || current.isSubmitting
        ) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            val result = courseRepository.createCourse(
                CourseCreateInput(
                    region = region,
                    startDate = startDate.toString(),
                    endDate = endDate.toString(),
                    workStartTime = startTime.toString(),
                    workEndTime = endTime.toString(),
                    workFocusLevel = workFocusLevel.name,
                    dogId = dogId,
                    name = "${region.label} ${formatTripDuration(startDate.toString(), endDate.toString())} 워케이션",
                )
            )

            _state.update { it.copy(isSubmitting = false) }

            result
                .onSuccess { course ->
                    _sideEffect.send(CreateCourseSideEffect.ShowToast("코스가 생성되었어요"))
                    _sideEffect.send(CreateCourseSideEffect.NavigateToResult(course.id))
                }
                .onFailure {
                    _sideEffect.send(CreateCourseSideEffect.ShowToast("코스 생성에 실패했어요"))
                }
        }
    }
}
