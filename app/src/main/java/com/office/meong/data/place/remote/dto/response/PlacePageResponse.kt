package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacePageResponse(
    @SerialName("content")
    val content: List<PlaceSummaryResponse> = emptyList(),
    @SerialName("page")
    val page: Int = 0,
    @SerialName("size")
    val size: Int = 0,
    @SerialName("totalElements")
    val totalElements: Int = 0,
    @SerialName("totalPages")
    val totalPages: Int = 0,
    @SerialName("hasNext")
    val hasNext: Boolean = false,
)
