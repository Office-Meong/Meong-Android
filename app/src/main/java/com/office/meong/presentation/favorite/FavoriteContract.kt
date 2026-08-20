package com.office.meong.presentation.favorite

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.presentation.favorite.model.FavoriteUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class FavoriteState(
    val favorites: UiState<ImmutableList<FavoriteUiModel>> = UiState.Loading,
    val selectedRegion: Region? = null,
    val selectedType: PlaceType? = null,
)

sealed interface FavoriteSideEffect {
    data class ShowSnackBar(val message: String) : FavoriteSideEffect
}
