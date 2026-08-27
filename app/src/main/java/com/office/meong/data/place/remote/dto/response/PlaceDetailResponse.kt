package com.office.meong.data.place.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("address")
    val address: String,
    @SerialName("tel")
    val tel: String?,
    @SerialName("homepage")
    val homepage: String?,
    @SerialName("overview")
    val overview: String?,
    @SerialName("imageUrls")
    val imageUrls: List<String>,
    @SerialName("score")
    val score: PlaceScoreResponse,
    @SerialName("petCondition")
    val petCondition: PlacePetConditionResponse,
    @SerialName("operation")
    val operation: PlaceOperationResponse,
    @SerialName("accessibility")
    val accessibility: PlaceAccessibilityResponse,
    @SerialName("favorite")
    val favorite: Boolean,
)
