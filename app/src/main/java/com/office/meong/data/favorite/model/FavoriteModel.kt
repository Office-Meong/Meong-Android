package com.office.meong.data.favorite.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.favorite.remote.dto.response.FavoriteResponse

data class FavoriteModel(
    val placeId: Long,
    val placeName: String,
    val placeType: String,
    val region: Region,
    val address: String,
    val thumbnailUrl: String?,
    val grade: String,
    val favoritedAt: String,
)

fun FavoriteResponse.toModel(): FavoriteModel = FavoriteModel(
    placeId = placeId,
    placeName = placeName,
    placeType = placeType,
    region = region,
    address = address,
    thumbnailUrl = thumbnailUrl,
    grade = grade,
    favoritedAt = favoritedAt,
)
