package com.office.meong.presentation.course.result.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ScheduleUiModel(
    val id: String,
    val placeType: PlaceType,
    val placeName: String,
    val grade: String,
    val location: String = ""
) {
    companion object {
        val DUMMY_SEARCHABLE_PLACES = persistentListOf(
            ScheduleUiModel(id = "search-1", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A"),
            ScheduleUiModel(id = "search-2", placeType = PlaceType.RESTAURANT, placeName = "댕댕이 맛집", grade = "A"),
            ScheduleUiModel(id = "search-3", placeType = PlaceType.SIGHTSEEING, placeName = "산책하기 좋은 공원", grade = "A"),
        )
    }
}
