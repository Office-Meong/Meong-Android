package com.office.meong.presentation.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.refresh.MeongPullToRefreshBox
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.core.trigger.LocalRefreshState
import com.office.meong.presentation.explore.component.ExploreEmptyContent
import com.office.meong.presentation.explore.component.ExploreSearchBar
import com.office.meong.presentation.explore.model.ExplorePlaceUiModel
import com.office.meong.presentation.favorite.component.FavoriteChipArea
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged

private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun ExploreRoute(
    paddingValues: PaddingValues,
    navigateToDetail: (Long) -> Unit = {},
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchState = rememberTextFieldState()
    val listState = rememberLazyListState()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val refreshState = LocalRefreshState.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ExploreSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    LaunchedEffect(searchState) {
        snapshotFlow { searchState.text.toString() }
            .distinctUntilChanged()
            .collect { viewModel.onKeywordChanged(it) }
    }

    // 탭 재탭: 맨 위가 아니면 맨 위로, 맨 위면 새로고침.
    LaunchedEffect(Unit) {
        refreshState.events.collect {
            if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                viewModel.refresh()
            } else {
                listState.scrollToItem(0)
            }
        }
    }

    // 상세 화면에서 즐겨찾기를 바꾸고 돌아온 경우, 목록은 유지한 채 하트 상태만 다시 맞춘다.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.syncFavorites()
    }

    ExploreScreen(
        paddingValues = paddingValues,
        state = state,
        searchState = searchState,
        listState = listState,
        onRegionSelected = viewModel::onRegionSelected,
        onTypeSelected = viewModel::onTypeSelected,
        onFavoriteClick = viewModel::onFavoriteClick,
        onPlaceClick = navigateToDetail,
        onRetry = viewModel::retryPlaces,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::onLoadMore,
        onRetryLoadMore = viewModel::retryLoadMore
    )
}

@Composable
private fun ExploreScreen(
    paddingValues: PaddingValues,
    state: ExploreState,
    searchState: TextFieldState,
    listState: LazyListState,
    onRegionSelected: (Region?) -> Unit,
    onTypeSelected: (PlaceType?) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onPlaceClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onRetryLoadMore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.white)
            .padding(paddingValues)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ExploreSearchBar(
            state = searchState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        FavoriteChipArea(
            selectedRegion = state.selectedRegion,
            selectedType = state.selectedType,
            onRegionSelected = onRegionSelected,
            onTypeSelected = onTypeSelected,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MeongTheme.colors.gray100,
            thickness = 1.dp
        )

        MeongPullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (val places = state.places) {
                is UiState.Loading -> {
                    MeongLoadingIndicator(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Failure -> {
                    MeongLoadErrorView(
                        action = LoadErrorViewAction.Retry(onRetryClick = onRetry),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Empty -> {
                    ExploreEmptyContent()
                }

                is UiState.Success -> {
                    ExploreList(
                        places = places.data,
                        totalCount = state.totalCount,
                        isLoadingMore = state.isLoadingMore,
                        isLoadMoreError = state.isLoadMoreError,
                        hasNext = state.hasNext,
                        listState = listState,
                        onFavoriteClick = onFavoriteClick,
                        onPlaceClick = onPlaceClick,
                        onLoadMore = onLoadMore,
                        onRetryLoadMore = onRetryLoadMore,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ExploreList(
    places: ImmutableList<ExplorePlaceUiModel>,
    totalCount: Int,
    isLoadingMore: Boolean,
    isLoadMoreError: Boolean,
    hasNext: Boolean,
    listState: LazyListState,
    onFavoriteClick: (Long) -> Unit,
    onPlaceClick: (Long) -> Unit,
    onLoadMore: () -> Unit,
    onRetryLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 마지막에서 3칸 안쪽이 보이면 다음 페이지를 요청한다.
    // 스크롤뿐 아니라 페이지가 붙어 totalItemsCount 가 바뀔 때도 다시 확인해야
    // 끝에 머문 채로도 남은 페이지가 이어서 로드된다. onLoadMore 는 로딩 중·마지막
    // 페이지면 스스로 무시하므로 중복 호출은 안전하다.
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex to layoutInfo.totalItemsCount
        }.collect { (lastVisibleIndex, totalItemsCount) ->
            if (totalItemsCount > 0 && lastVisibleIndex >= totalItemsCount - LOAD_MORE_THRESHOLD) {
                onLoadMore()
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.background(color = MeongTheme.colors.gray50),
        contentPadding = PaddingValues(all = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "총 ${totalCount}개의 장소",
                color = MeongTheme.colors.gray700,
                style = MeongTheme.typography.label.label14Sb
            )
        }

        items(
            items = places,
            key = { it.placeId }
        ) { place ->
            MeongPlaceCard(
                placeName = place.placeName,
                location = place.address,
                placeType = place.placeType,
                grade = place.grade,
                thumbnailUrl = place.thumbnailUrl,
                isFavorite = place.isFavorite,
                onFavoriteClick = { onFavoriteClick(place.placeId) },
                modifier = Modifier.noRippleClickable { onPlaceClick(place.placeId) }
            )
        }

        when {
            isLoadingMore -> item {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )
            }

            isLoadMoreError -> item {
                Text(
                    text = "장소를 더 불러오지 못했어요. 다시 시도",
                    color = MeongTheme.colors.gray700,
                    style = MeongTheme.typography.body.body12M,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable { onRetryLoadMore() }
                        .padding(vertical = 16.dp)
                )
            }

            !hasNext -> item {
                Text(
                    text = "모든 장소를 불러왔어요",
                    color = MeongTheme.colors.gray500,
                    style = MeongTheme.typography.body.body12M,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ExploreScreenPreview() {
    MeongTheme {
        ExploreScreen(
            paddingValues = PaddingValues(),
            state = ExploreState(
                places = UiState.Success(
                    persistentListOf(
                        ExplorePlaceUiModel(
                            placeId = 1,
                            placeName = "몽멍이 카페",
                            address = "서울시 강남구",
                            placeType = PlaceType.RESTAURANT,
                            grade = "A",
                            thumbnailUrl = null,
                            isFavorite = true
                        ),
                        ExplorePlaceUiModel(
                            placeId = 2,
                            placeName = "몽멍이 카페",
                            address = "서울시 강남구",
                            placeType = PlaceType.RESTAURANT,
                            grade = "A",
                            thumbnailUrl = "",
                            isFavorite = true
                        ),

                    )
                )
            ),
            searchState = rememberTextFieldState(),
            listState = rememberLazyListState(),
            onRegionSelected = {},
            onTypeSelected = {},
            onFavoriteClick = {},
            onPlaceClick = {},
            onRetry = {},
            onRefresh = {},
            onLoadMore = {},
            onRetryLoadMore = {}
        )
    }
}
