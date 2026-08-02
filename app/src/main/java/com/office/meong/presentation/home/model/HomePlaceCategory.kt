package com.office.meong.presentation.home.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType

@Immutable
data class HomePlaceCategory(
    val type: PlaceType,
    val count: Int
)
