package com.office.meong.data.favorite.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponse(
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("region")
    val region: Region,
    @SerialName("address")
    val address: String,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String?,
    @SerialName("grade")
    val grade: String,
    @SerialName("favoritedAt")
    val favoritedAt: String,
)
