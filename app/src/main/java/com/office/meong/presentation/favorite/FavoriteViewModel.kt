package com.office.meong.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.awaitMinDuration
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

    // 최초 로드는 화면의 onResume 에서 refresh() 로 트리거한다(init 조회 + onResume 조회로 두 번 나가는 것을 방지).

    fun retryFavorites() {
        fetchFavorites(userInitiated = true)
    }

    /**
     * @param userInitiated 당겨서 새로고침 / 탭 재탭이면 true — 인디케이터를 노출한다.
     *   false(화면 복귀)면 조용히 갱신한다.
     */
    fun refresh(userInitiated: Boolean = false) {
        fetchFavorites(userInitiated = userInitiated)
    }

    fun onRegionSelected(region: Region?) {
        _state.update { it.copy(selectedRegion = region, favorites = UiState.Loading) }
        fetchFavorites(userInitiated = false)
    }

    fun onTypeSelected(type: PlaceType?) {
        _state.update { it.copy(selectedType = type, favorites = UiState.Loading) }
        fetchFavorites(userInitiated = false)
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

    private fun fetchFavorites(userInitiated: Boolean) {
        viewModelScope.launch {
            // 이미 결과(목록 또는 빈 상태)를 들고 있으면 배경 새로고침으로 간주해 로딩·실패 화면을 띄우지 않는다.
            val current = _state.value.favorites
            val isBackgroundRefresh = current !is UiState.Loading && current !is UiState.Failure
            if (!isBackgroundRefresh) _state.update { it.copy(favorites = UiState.Loading) }
            if (userInitiated) _state.update { it.copy(isRefreshing = true) }
            val startedAtMs = System.currentTimeMillis()

            val region = _state.value.selectedRegion
            val placeType = _state.value.selectedType?.apiValue

            val result = favoriteRepository.getFavorites(region = region, placeType = placeType)
            if (userInitiated) awaitMinDuration(startedAtMs)

            result
                .onSuccess { favorites ->
                    _state.update {
                        it.copy(
                            favorites = if (favorites.isEmpty()) {
                                UiState.Empty
                            } else {
                                UiState.Success(favorites.map { favorite -> favorite.toUiModel() }.toImmutableList())
                            },
                            isRefreshing = false,
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            favorites = if (!isBackgroundRefresh) {
                                UiState.Failure(LoadErrorHandleAction.Retry)
                            } else {
                                it.favorites
                            },
                            isRefreshing = false,
                        )
                    }
                }
        }
    }
}
