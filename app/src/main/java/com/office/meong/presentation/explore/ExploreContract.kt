package com.office.meong.presentation.explore

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.presentation.explore.model.ExplorePlaceUiModel
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class ExploreState(
    val places: UiState<ImmutableList<ExplorePlaceUiModel>> = UiState.Loading,
    val totalCount: Int = 0,
    val selectedRegion: Region? = null,
    val selectedType: PlaceType? = null,
    val keyword: String = "",
    val page: Int = 0,
    val hasNext: Boolean = false,
    val isLoadingMore: Boolean = false,
)

sealed interface ExploreSideEffect {
    data class ShowSnackBar(val message: String) : ExploreSideEffect
}
