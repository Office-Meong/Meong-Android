package com.office.meong.presentation.explore.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType
import com.office.meong.data.place.model.PlaceSummary

@Immutable
data class ExplorePlaceUiModel(
    val placeId: Long,
    val placeName: String,
    val address: String,
    val placeType: PlaceType,
    val grade: String,
    val thumbnailUrl: String?,
    val isFavorite: Boolean,
)

fun PlaceSummary.toUiModel() = ExplorePlaceUiModel(
    placeId = id,
    placeName = name,
    address = address,
    placeType = PlaceType.from(placeType),
    grade = grade,
    thumbnailUrl = thumbnailUrl,
    isFavorite = favorite,
)
