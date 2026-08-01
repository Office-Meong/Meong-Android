package com.office.meong.presentation.course.my.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType

@Immutable
data class MyCoursePlaceCategory(
    val type: PlaceType,
    val count: Int
)
