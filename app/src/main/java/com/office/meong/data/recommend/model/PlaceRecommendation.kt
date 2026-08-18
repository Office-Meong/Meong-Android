package com.office.meong.data.recommend.model

import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.recommend.remote.dto.response.PlaceRecommendationResponse

data class PlaceRecommendation(
    val placeId: Long,
    val placeName: String,
    val region: Region,
    val placeType: PlaceType,
    val address: String,
    val totalScore: Int,
    val grade: String,
    val reason: String,
)

fun PlaceRecommendationResponse.toModel(): PlaceRecommendation = PlaceRecommendation(
    placeId = placeId,
    placeName = placeName,
    region = region,
    placeType = PlaceType.from(placeType),
    address = address,
    totalScore = totalScore,
    grade = grade,
    reason = reason,
)
