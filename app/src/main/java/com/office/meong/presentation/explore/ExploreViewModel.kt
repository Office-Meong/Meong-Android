package com.office.meong.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.awaitMinDuration
import com.office.meong.core.common.util.successData
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.favorite.repository.FavoriteRepository
import com.office.meong.data.place.model.PlacePage
import com.office.meong.domain.favorite.usecase.ToggleFavoriteUseCase
import com.office.meong.domain.place.model.PlaceSearchQuery
import com.office.meong.domain.place.usecase.PlaceSearchUseCase
import com.office.meong.presentation.explore.model.ExplorePlaceUiModel
import com.office.meong.presentation.explore.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreState())
    val state: StateFlow<ExploreState> = _state.asStateFlow()

    private val _sideEffect = Channel<ExploreSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    // 같은 조건으로 다시 넣어도(재시도 등) 그대로 방출되도록 StateFlow 대신 SharedFlow 를 쓴다.
    private val queries = MutableSharedFlow<PlaceSearchQuery>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    private data class SnapshotKey(val region: Region?, val type: PlaceType?, val keyword: String)
    private data class Snapshot(
        val places: ImmutableList<ExplorePlaceUiModel>,
        val totalCount: Int,
        val hasNext: Boolean,
    )

    // 필터별 직전 0페이지 결과. 필터 토글 시 스피너 없이 즉시 보여주기 위한 세션 캐시.
    // 접근 순서 LRU, 12개 상한.
    private val snapshots = object : LinkedHashMap<SnapshotKey, Snapshot>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<SnapshotKey, Snapshot>?) = size > MAX_SNAPSHOTS
    }

    init {
        viewModelScope.launch {
            placeSearchUseCase.search(queries).collect(::handleResult)
        }
        submitQuery(page = 0)
    }

    fun retryPlaces() {
        _state.update { it.copy(places = UiState.Loading) }
        submitQuery(page = 0)
    }

    private var refreshStartedAtMs = 0L

    /** 당겨서 새로고침 / 탭 재탭(맨 위). 캐시를 비우고 인디케이터를 노출하며 전체를 다시 받는다. */
    fun refresh() {
        if (_state.value.isRefreshing) return
        snapshots.clear()
        refreshStartedAtMs = System.currentTimeMillis()
        _state.update { it.copy(isRefreshing = true) }
        submitQuery(page = 0)
    }

    fun onRegionSelected(region: Region?) {
        if (_state.value.selectedRegion == region) return
        _state.update { it.copy(selectedRegion = region) }
        applyFilterChange()
    }

    fun onTypeSelected(type: PlaceType?) {
        if (_state.value.selectedType == type) return
        _state.update { it.copy(selectedType = type) }
        applyFilterChange()
    }

    fun onKeywordChanged(keyword: String) {
        if (_state.value.keyword == keyword) return
        _state.update { it.copy(keyword = keyword) }
        applyFilterChange()
    }

    private fun applyFilterChange() {
        val snapshot = snapshots[currentSnapshotKey()]
        if (snapshot != null) {
            // 캐시된 결과를 스피너 없이 즉시 반영한다(백그라운드 재조회 없음).
            _state.update {
                it.copy(
                    places = if (snapshot.places.isEmpty()) UiState.Empty else UiState.Success(snapshot.places),
                    totalCount = snapshot.totalCount,
                    page = 0,
                    hasNext = snapshot.hasNext,
                    isLoadingMore = false,
                )
            }
        } else {
            _state.update { it.copy(places = UiState.Loading, page = 0) }
            submitQuery(page = 0)
        }
    }

    fun onLoadMore() {
        val current = _state.value
        if (current.isLoadingMore || !current.hasNext || current.places !is UiState.Success) return

        _state.update { it.copy(isLoadingMore = true) }
        submitQuery(page = current.page + 1)
    }

    /**
     * 화면 복귀 시 호출. 목록은 유지한 채(saveState) 상세 화면 등에서 바뀐 즐겨찾기만 덮어쓴다.
     */
    fun syncFavorites() {
        if (_state.value.places !is UiState.Success) return

        viewModelScope.launch {
            favoriteRepository.getFavorites()
                .onSuccess { favorites ->
                    val favoritedIds = favorites.mapTo(HashSet()) { it.placeId }
                    var changed = false
                    _state.update { current ->
                        val places = (current.places as? UiState.Success)?.data ?: return@update current
                        val synced = places.map { place ->
                            val shouldBeFavorite = place.placeId in favoritedIds
                            if (place.isFavorite != shouldBeFavorite) changed = true
                            place.copy(isFavorite = shouldBeFavorite)
                        }.toImmutableList()
                        current.copy(places = UiState.Success(synced))
                    }
                    if (changed) snapshots.clear()
                }
        }
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
                    // 캐시된 목록의 favorite 값이 낡았으므로 비운다.
                    snapshots.clear()
                }
                .onFailure {
                    _sideEffect.send(ExploreSideEffect.ShowSnackBar("즐겨찾기 처리에 실패했어요"))
                }
        }
    }

    private fun currentSnapshotKey(): SnapshotKey {
        val current = _state.value
        return SnapshotKey(
            region = current.selectedRegion,
            type = current.selectedType,
            keyword = current.keyword.trim(),
        )
    }

    private fun submitQuery(page: Int) {
        val current = _state.value
        queries.tryEmit(
            PlaceSearchQuery(
                keyword = current.keyword.ifBlank { null },
                region = current.selectedRegion,
                type = current.selectedType?.apiValue,
                page = page,
            )
        )
    }

    private suspend fun handleResult(result: Result<PlacePage>) {
        val wasRefreshing = _state.value.isRefreshing
        if (wasRefreshing) awaitMinDuration(refreshStartedAtMs)

        result
            .onSuccess { page ->
                val incoming = page.content.map { it.toUiModel() }

                if (page.page == 0) {
                    snapshots[currentSnapshotKey()] = Snapshot(
                        places = incoming.toImmutableList(),
                        totalCount = page.totalElements,
                        hasNext = page.hasNext,
                    )
                }

                _state.update { current ->
                    val merged = (
                        if (page.page == 0) {
                            incoming
                        } else {
                            (current.places.successData.orEmpty() + incoming).distinctBy { it.placeId }
                        }
                        ).toImmutableList()

                    current.copy(
                        places = if (merged.isEmpty()) UiState.Empty else UiState.Success(merged),
                        totalCount = page.totalElements,
                        page = page.page,
                        hasNext = page.hasNext,
                        isLoadingMore = false,
                        isRefreshing = false,
                    )
                }
            }
            .onFailure {
                val wasLoadingMore = _state.value.isLoadingMore
                _state.update { it.copy(isLoadingMore = false, isRefreshing = false) }

                if (wasLoadingMore) {
                    _sideEffect.send(ExploreSideEffect.ShowSnackBar("장소를 더 불러오지 못했어요"))
                } else if (wasRefreshing) {
                    _sideEffect.send(ExploreSideEffect.ShowSnackBar("새로고침에 실패했어요"))
                } else {
                    _state.update { it.copy(places = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
            }
    }

    companion object {
        private const val MAX_SNAPSHOTS = 12
    }
}
