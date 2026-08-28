package com.office.meong.data.place.model

import com.office.meong.data.place.remote.dto.response.PlaceSummaryResponse

data class PlaceSummary(
    val id: Long,
    val name: String,
    val placeType: String,
    val address: String,
    val thumbnailUrl: String?,
    val grade: String,
    val favorite: Boolean,
)

fun PlaceSummaryResponse.toModel(): PlaceSummary = PlaceSummary(
    id = id,
    name = name,
    placeType = placeType,
    address = address,
    thumbnailUrl = thumbnailUrl,
    grade = grade,
    favorite = favorite,
)
