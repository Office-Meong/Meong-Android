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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
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
        onLoadMore = viewModel::onLoadMore
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
    onLoadMore: () -> Unit
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
                        listState = listState,
                        onFavoriteClick = onFavoriteClick,
                        onPlaceClick = onPlaceClick,
                        onLoadMore = onLoadMore,
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
    listState: LazyListState,
    onFavoriteClick: (Long) -> Unit,
    onPlaceClick: (Long) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= listState.layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) onLoadMore()
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

        if (isLoadingMore) {
            item {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
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
            onLoadMore = {}
        )
    }
}
