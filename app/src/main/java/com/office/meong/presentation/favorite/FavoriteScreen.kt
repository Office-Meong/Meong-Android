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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.favorite.component.FavoriteChipArea
import com.office.meong.presentation.favorite.model.FavoriteUiModel
import com.office.meong.presentation.sharedcomponent.CourseEmptyContent
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteRoute(
    paddingValues: PaddingValues,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

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
        onRegionSelected = viewModel::onRegionSelected,
        onTypeSelected = viewModel::onTypeSelected,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRetry = viewModel::retryFavorites
    )
}

@Composable
private fun FavoriteScreen(
    paddingValues: PaddingValues,
    state: FavoriteState,
    onRegionSelected: (Region?) -> Unit,
    onTypeSelected: (PlaceType?) -> Unit,
    onFavoriteClick: (Long) -> Unit,
    onRetry: () -> Unit
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

        when (val favorites = state.favorites) {
            is UiState.Loading -> {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            is UiState.Failure -> {
                MeongLoadErrorView(
                    action = LoadErrorViewAction.Retry(onRetryClick = onRetry),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }

            is UiState.Empty -> {
                CourseEmptyContent(
                    onClickPillButton = {},
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(color = MeongTheme.colors.gray50)
                        .padding(20.dp)
                )
            }

            is UiState.Success -> {
                FavoriteList(
                    favorites = favorites.data,
                    totalCount = state.favorites.successData?.size ?: 0,
                    onFavoriteClick = onFavoriteClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun FavoriteList(
    favorites: ImmutableList<FavoriteUiModel>,
    totalCount: Int,
    onFavoriteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
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
                onFavoriteClick = { onFavoriteClick(favorite.placeId) }
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
            onRegionSelected = {},
            onTypeSelected = {},
            onFavoriteClick = {},
            onRetry = {}
        )
    }
}
