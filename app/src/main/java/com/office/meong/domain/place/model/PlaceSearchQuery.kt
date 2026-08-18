package com.office.meong.domain.place.model

import com.office.meong.core.model.region.Region

data class PlaceSearchQuery(
    val keyword: String? = null,
    val region: Region? = null,
    val type: String? = null,
    val page: Int = 0,
    val size: Int = 20,
)
