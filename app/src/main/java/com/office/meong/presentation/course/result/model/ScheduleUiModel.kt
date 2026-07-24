package com.office.meong.presentation.course.result.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType

@Immutable
data class ScheduleUiModel(
    val id: String,
    val placeType: PlaceType,
    val placeName: String,
    val grade: String,
    val location: String = ""
)
