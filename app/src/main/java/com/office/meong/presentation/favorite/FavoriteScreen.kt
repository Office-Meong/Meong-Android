package com.office.meong.presentation.favorite

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.office.meong.core.common.util.successData
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
import com.office.meong.presentation.favorite.component.FavoriteChipArea
import com.office.meong.presentation.favorite.component.FavoriteEmptyView
import com.office.meong.presentation.favorite.model.FavoriteUiModel
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteRoute(
    paddingValues: PaddingValues,
    navigateToExplore: () -> Unit,
    navigateToDetail: (Long) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val refreshState = LocalRefreshState.current

    // 상세 화면에서 즐겨찾기를 해제하고 돌아온 경우 등을 반영하기 위해 복귀 시 다시 조회한다.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    // 탭 재탭: 맨 위가 아니면 맨 위로, 맨 위면 새로고침.
    LaunchedEffect(Unit) {
        refreshState.events.collect {
            if (listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                viewModel.refresh(userInitiated = true)
            } else {
                listState.scrollToItem(0)
            }
        }
    }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is FavoriteSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    FavoriteScreen(
        paddingValues = paddingValues,
        state = state,
        listState = listState,
        onRegionSelected = viewModel::onRegionSelected,
        onTypeSelected = viewModel::onTypeSelected,
        onFavoriteClick = viewModel::onFavoriteClick,
        onPlaceClick = navigateToDetail,
        onRetry = viewModel::retryFavorites,
        onRefresh = { viewModel.refresh(userInitiated = true) },
        navigateToExplore = navigateToExplore
    )
}

@Composable
private fun FavoriteScreen(
    paddingValues: PaddingValues,
    state: FavoriteState,
    listState: LazyListState,
    onRegionSelected: (Region?) -> Unit,
    onTypeSelected: (PlaceType?) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onPlaceClick: (Long) -> Unit,
    onRetry: () -> Unit,
    onRefresh: () -> Unit,
    navigateToExplore: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.white)
            .padding(paddingValues)
    ) {
        Text(
            text = "관심 장소",
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp)
        )

        Spacer(modifier = Modifier.height(34.dp))

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
            when (val favorites = state.favorites) {
                is UiState.Loading -> {
                    MeongLoadingIndicator(modifier = Modifier.fillMaxSize())
                }

                is UiState.Failure -> {
                    MeongLoadErrorView(
                        action = LoadErrorViewAction.Retry(onRetryClick = onRetry),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Empty -> {
                    FavoriteEmptyView(
                        navigateToExplore = navigateToExplore,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is UiState.Success -> {
                    FavoriteList(
                        favorites = favorites.data,
                        totalCount = state.favorites.successData?.size ?: 0,
                        listState = listState,
                        onFavoriteClick = onFavoriteClick,
                        onPlaceClick = onPlaceClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoriteList(
    favorites: ImmutableList<FavoriteUiModel>,
    totalCount: Int,
    listState: LazyListState,
    onFavoriteClick: (Long) -> Unit,
    onPlaceClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            items = favorites,
            key = { it.placeId }
        ) { favorite ->
            MeongPlaceCard(
                placeName = favorite.placeName,
                location = favorite.address,
                placeType = favorite.placeType,
                grade = favorite.grade,
                thumbnailUrl = favorite.thumbnailUrl,
                isFavorite = true,
                onFavoriteClick = { onFavoriteClick(favorite.placeId) },
                modifier = Modifier.noRippleClickable { onPlaceClick(favorite.placeId) }
            )
        }
    }
}

@Preview
@Composable
private fun FavoriteScreenPreview() {
    MeongTheme {
        FavoriteScreen(
            paddingValues = PaddingValues(),
            state = FavoriteState(),
            listState = rememberLazyListState(),
            onRegionSelected = {},
            onTypeSelected = {},
            onFavoriteClick = {},
            onPlaceClick = {},
            onRetry = {},
            onRefresh = {},
            navigateToExplore = {}
        )
    }
}
