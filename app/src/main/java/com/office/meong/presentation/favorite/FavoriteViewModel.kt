package com.office.meong.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.favorite.repository.FavoriteRepository
import com.office.meong.presentation.favorite.model.toUiModel
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
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteState())
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    private val _sideEffect = Channel<FavoriteSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        fetchFavorites()
    }

    fun retryFavorites() {
        fetchFavorites()
    }

    fun onRegionSelected(region: Region?) {
        _state.update { it.copy(selectedRegion = region) }
        fetchFavorites()
    }

    fun onTypeSelected(type: PlaceType?) {
        _state.update { it.copy(selectedType = type) }
        fetchFavorites()
    }

    fun onFavoriteClick(placeId: Long) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(placeId)
                .onSuccess {
                    _state.update { current ->
                        val favorites = current.favorites
                        if (favorites !is UiState.Success) return@update current

                        val remaining = favorites.data.filterNot { it.placeId == placeId }.toImmutableList()
                        current.copy(
                            favorites = if (remaining.isEmpty()) UiState.Empty else UiState.Success(remaining)
                        )
                    }
                }
                .onFailure {
                    _sideEffect.send(FavoriteSideEffect.ShowSnackBar("즐겨찾기 취소에 실패했어요"))
                }
        }
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(favorites = UiState.Loading) }

            val region = _state.value.selectedRegion
            val placeType = _state.value.selectedType?.apiValue

            favoriteRepository.getFavorites(region = region, placeType = placeType)
                .onSuccess { favorites ->
                    _state.update {
                        it.copy(
                            favorites = if (favorites.isEmpty()) {
                                UiState.Empty
                            } else {
                                UiState.Success(favorites.map { favorite -> favorite.toUiModel() }.toImmutableList())
                            }
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(favorites = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }
}
