package com.office.meong.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.place.model.PlacePage
import com.office.meong.domain.favorite.usecase.ToggleFavoriteUseCase
import com.office.meong.domain.place.model.PlaceSearchQuery
import com.office.meong.domain.place.usecase.PlaceSearchUseCase
import com.office.meong.presentation.explore.model.toUiModel
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
class ExploreViewModel @Inject constructor(
    private val placeSearchUseCase: PlaceSearchUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state.asStateFlow()

    private val _sideEffect = Channel<ExploreSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    private val queries = MutableStateFlow(PlaceSearchQuery())

    init {
        viewModelScope.launch {
            placeSearchUseCase.search(queries).collect(::handleResult)
        }
    }

    fun retryPlaces() {
        submitQuery(page = 0)
    }

    fun onRegionSelected(region: Region?) {
        _state.update { it.copy(selectedRegion = region) }
        submitQuery(page = 0)
    }

    fun onTypeSelected(type: PlaceType?) {
        _state.update { it.copy(selectedType = type) }
        submitQuery(page = 0)
    }

    fun onKeywordChanged(keyword: String) {
        if (_state.value.keyword == keyword) return
        _state.update {
            it.copy(keyword = keyword)
        }
        submitQuery(page = 0)
    }

    fun onLoadMore() {
        val current = _state.value
        if (current.isLoadingMore || !current.hasNext || current.places !is UiState.Success) return

        _state.update { it.copy(isLoadingMore = true) }
        submitQuery(page = current.page + 1)
    }

    fun onFavoriteClick(placeId: Long) {
        val places = (_state.value.places as? UiState.Success)?.data ?: return
        val place = places.firstOrNull { it.placeId == placeId } ?: return

        viewModelScope.launch {
            toggleFavoriteUseCase.toggle(placeId, place.isFavorite)
                .onSuccess {
                    _state.update { current ->
                        val currentPlaces = (current.places as? UiState.Success)?.data ?: return@update current
                        val updated = currentPlaces.map {
                            if (it.placeId == placeId) it.copy(isFavorite = !it.isFavorite) else it
                        }.toImmutableList()
                        current.copy(places = UiState.Success(updated))
                    }
                }
                .onFailure {
                    _sideEffect.send(ExploreSideEffect.ShowSnackBar("즐겨찾기 처리에 실패했어요"))
                }
        }
    }

    private fun submitQuery(page: Int) {
        if (page == 0) {
            _state.update { it.copy(places = UiState.Loading, page = 0) }
        }

        val current = _state.value
        queries.update {
            it.copy(
                keyword = current.keyword.ifBlank { null },
                region = current.selectedRegion,
                type = current.selectedType?.apiValue,
                page = page,
            )
        }
    }

    private suspend fun handleResult(result: Result<PlacePage>) {
        result
            .onSuccess { page ->
                _state.update { current ->
                    val incoming = page.content.map { it.toUiModel() }
                    val merged = (
                        if (page.page == 0) {
                            incoming
                        } else {
                            (current.places.successData.orEmpty() + incoming)
                                .distinctBy { it.placeId }
                        }
                        ).toImmutableList()

                    current.copy(
                        places = if (merged.isEmpty()) UiState.Empty else UiState.Success(merged),
                        totalCount = page.totalElements,
                        page = page.page,
                        hasNext = page.hasNext,
                        isLoadingMore = false,
                    )
                }
            }
            .onFailure {
                val wasLoadingMore = _state.value.isLoadingMore
                _state.update { it.copy(isLoadingMore = false) }

                if (wasLoadingMore) {
                    _sideEffect.send(ExploreSideEffect.ShowSnackBar("장소를 더 불러오지 못했어요"))
                } else {
                    _state.update { it.copy(places = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
            }
    }
}
