package com.office.meong.presentation.favorite.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType
import com.office.meong.data.favorite.model.FavoriteModel

@Immutable
data class FavoriteUiModel(
    val placeId: Long,
    val placeName: String,
    val address: String,
    val placeType: PlaceType,
    val grade: String,
    val thumbnailUrl: String?,
)

fun FavoriteModel.toUiModel() = FavoriteUiModel(
    placeId = placeId,
    placeName = placeName,
    address = address,
    placeType = PlaceType.from(placeType),
    grade = grade,
    thumbnailUrl = thumbnailUrl,
)
