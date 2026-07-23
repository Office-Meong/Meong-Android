package com.office.meong.presentation.home.model

import androidx.compose.runtime.Immutable

@Immutable
data class HomePlaceCategory(
    val type: PlaceType,
    val count: Int
)
