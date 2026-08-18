package com.office.meong.data.place.model

import com.office.meong.data.place.remote.dto.response.PlacePageResponse

data class PlacePage(
    val content: List<PlaceSummary>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val hasNext: Boolean,
)

fun PlacePageResponse.toModel(): PlacePage = PlacePage(
    content = content.map { it.toModel() },
    page = page,
    size = size,
    totalElements = totalElements,
    totalPages = totalPages,
    hasNext = hasNext,
)
