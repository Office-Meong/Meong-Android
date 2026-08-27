package com.office.meong.data.recommend.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceRecommendationResponse(
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("address")
    val address: String,
    @SerialName("totalScore")
    val totalScore: Int,
    @SerialName("grade")
    val grade: String,
    @SerialName("reason")
    val reason: String
)
