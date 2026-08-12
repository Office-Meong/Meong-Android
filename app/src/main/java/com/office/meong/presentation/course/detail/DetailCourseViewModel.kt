package com.office.meong.presentation.course.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.data.course.repository.CourseRepository
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
) : ViewModel() {
    private val courseId = savedStateHandle.toRoute<DetailCourse>().courseId

    private val _state = MutableStateFlow(DetailCourseState())
    val state: StateFlow<DetailCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<DetailCourseSideEffect>()
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

    private fun fetchCourseDetail() {
        viewModelScope.launch {
            _state.update { it.copy(course = UiState.Loading) }

            courseRepository.getDetailCourse(courseId)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(course), selectedDayNumber = 1) }
                }
                .onFailure {
                    _state.update { it.copy(course = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("코스 정보를 불러오지 못했어요"))
                }
        }
    }
}
