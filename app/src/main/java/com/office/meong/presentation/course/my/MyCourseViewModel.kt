package com.office.meong.presentation.course.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.presentation.course.my.model.toUiModel
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
class MyCourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyCourseState())
    val state: StateFlow<MyCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<MyCourseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchMyCourses()
    }

    fun retryMyCourses() {
        fetchMyCourses()
    }

    private fun fetchMyCourses() {
        viewModelScope.launch {
            _state.update { it.copy(myCoursesSummaries = UiState.Loading) }

            courseRepository.getCourses()
                .onSuccess { courses ->
                    _state.update { currentState ->
                        currentState.copy(
                            myCoursesSummaries = UiState.Success(courses.map { it.toUiModel() }.toImmutableList())
                        )
                    }
                }
                .onFailure {
                    _state.update { currentState ->
                        currentState.copy(
                            myCoursesSummaries = UiState.Failure(LoadErrorHandleAction.Retry)
                        )
                    }
                }
        }
    }
}
