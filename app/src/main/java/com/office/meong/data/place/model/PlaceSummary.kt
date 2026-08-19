package com.office.meong.data.place.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.place.remote.dto.response.PlaceSummaryResponse

data class PlaceSummary(
    val id: Long,
    val name: String,
    val region: Region,
    val placeType: String,
    val address: String,
    val thumbnailUrl: String?,
    val grade: String,
    val totalScore: Int,
    val acmpyType: String,
    val congestionScore: Int,
    val congestionLevel: String,
    val favorite: Boolean,
)

fun PlaceSummaryResponse.toModel(): PlaceSummary = PlaceSummary(
    id = id,
    name = name,
    region = region,
    placeType = placeType,
    address = address,
    thumbnailUrl = thumbnailUrl,
    grade = grade,
    totalScore = totalScore,
    acmpyType = acmpyType,
    congestionScore = congestionScore,
    congestionLevel = congestionLevel,
    favorite = favorite,
)
