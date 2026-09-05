package com.office.meong.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.presentation.home.model.toUiModel
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
class HomeViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val petRepository: PetRepository,
): ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    // 최초 로드는 화면의 onResume 에서 refresh() 로 트리거한다(init + onResume 로 두 번 조회하는 것을 방지).

    fun retryLoad() {
        fetchHomeCourses()
    }

    fun retryPetInfo() {
        fetchPetInfo()
    }

    /** 화면 진입·복귀 시 호출. 결과가 이미 있으면 조용히, 없으면 로딩을 노출하며 다시 불러온다. */
    fun refresh() {
        fetchHomeCourses()
        fetchPetInfo()
    }

    private fun fetchPetInfo() {
        viewModelScope.launch {
            val current = _state.value.petInfo
            val isBackgroundRefresh = current !is UiState.Loading && current !is UiState.Failure
            if (!isBackgroundRefresh) _state.update { it.copy(petInfo = UiState.Loading) }

            petRepository.getDogs()
                .onSuccess { petInfo ->
                    _state.update { currentState ->
                        currentState.copy(
                            petInfo = petInfo.firstOrNull()?.toInfo()
                                ?.let { UiState.Success(it) }
                                ?: UiState.Empty
                        )
                    }
                }
                .onFailure {
                    if (!isBackgroundRefresh) {
                        _state.update { it.copy(petInfo = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    }
                }
        }
    }

    private fun fetchHomeCourses() {
        viewModelScope.launch {
            val current = _state.value.homeCourseSummaries
            val isBackgroundRefresh = current !is UiState.Loading && current !is UiState.Failure
            if (!isBackgroundRefresh) _state.update { it.copy(homeCourseSummaries = UiState.Loading) }

            courseRepository.getCourses()
                .onSuccess { courseResult ->
                    _state.update { currentState ->
                        currentState.copy(
                            homeCourseSummaries = UiState.Success(
                                courseResult.map { it.toUiModel() }.toImmutableList()
                            )
                        )
                    }
                }
                .onFailure {
                    if (!isBackgroundRefresh) {
                        _state.update { it.copy(homeCourseSummaries = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    }
                }
        }
    }
}
