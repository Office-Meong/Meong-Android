package com.office.meong.presentation.explore.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.data.place.repository.PlaceRepository
import com.office.meong.domain.favorite.usecase.ToggleFavoriteUseCase
import com.office.meong.presentation.explore.detail.navigation.ExploreDetail
import com.office.meong.presentation.explore.detail.model.toUiModel
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
class ExploreDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val placeRepository: PlaceRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {
    private val placeId = savedStateHandle.toRoute<ExploreDetail>().placeId

    private val _state = MutableStateFlow(ExploreDetailState())
    val state: StateFlow<ExploreDetailState> = _state.asStateFlow()

    private val _sideEffect = Channel<ExploreDetailSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchPlaceDetail()
        fetchWalkCourses()
    }

    fun retry() {
        fetchPlaceDetail()
    }

    fun onFavoriteClick() {
        val current = _state.value.place.successData ?: return

        viewModelScope.launch {
            toggleFavoriteUseCase.toggle(placeId, current.isFavorite)
                .onSuccess {
                    _state.update { it.copy(place = UiState.Success(current.copy(isFavorite = !current.isFavorite))) }
                }
                .onFailure {
                    _sideEffect.send(ExploreDetailSideEffect.ShowSnackBar("즐겨찾기 처리에 실패했어요"))
                }
        }
    }

    private fun fetchPlaceDetail() {
        viewModelScope.launch {
            _state.update { it.copy(place = UiState.Loading) }

            placeRepository.getPlaceDetail(placeId)
                .onSuccess { detail ->
                    _state.update { it.copy(place = UiState.Success(detail.toUiModel())) }
                }
                .onFailure {
                    _state.update { it.copy(place = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    private fun fetchWalkCourses() {
        viewModelScope.launch {
            placeRepository.getPlaceWalkCourses(placeId)
                .onSuccess { courses ->
                    _state.update {
                        it.copy(
                            walkCourses = if (courses.isEmpty()) {
                                UiState.Empty
                            } else {
                                UiState.Success(courses.map { course -> course.toUiModel() }.toImmutableList())
                            }
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(walkCourses = UiState.Empty) }
                }
        }
    }
}
