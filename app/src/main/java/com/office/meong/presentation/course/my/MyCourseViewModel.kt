package com.office.meong.presentation.course.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.awaitMinDuration
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

    fun retryMyCourses() {
        fetchMyCourses(userInitiated = true)
    }

    /**
     * @param userInitiated 당겨서 새로고침 / 탭 재탭이면 true — 인디케이터를 노출하고 서버 캐시를 우회한다.
     *   false(화면 복귀)면 조용히 갱신한다.
     */
    fun refresh(userInitiated: Boolean = false) {
        fetchMyCourses(userInitiated = userInitiated)
    }

    private fun fetchMyCourses(userInitiated: Boolean) {
        viewModelScope.launch {
            // 이미 결과를 들고 있으면 배경 새로고침으로 간주해 로딩·실패 화면을 띄우지 않는다.
            val current = _state.value.myCoursesSummaries
            val isBackgroundRefresh = current !is UiState.Loading && current !is UiState.Failure
            if (!isBackgroundRefresh) _state.update { it.copy(myCoursesSummaries = UiState.Loading) }
            if (userInitiated) _state.update { it.copy(isRefreshing = true) }
            val startedAtMs = System.currentTimeMillis()

            val result = courseRepository.getCourses(forceRefresh = userInitiated)
            if (userInitiated) awaitMinDuration(startedAtMs)

            result
                .onSuccess { courses ->
                    _state.update {
                        it.copy(
                            myCoursesSummaries = UiState.Success(courses.map { c -> c.toUiModel() }.toImmutableList()),
                            isRefreshing = false,
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            myCoursesSummaries = if (!isBackgroundRefresh) {
                                UiState.Failure(LoadErrorHandleAction.Retry)
                            } else {
                                it.myCoursesSummaries
                            },
                            isRefreshing = false,
                        )
                    }
                }
        }
    }
}
