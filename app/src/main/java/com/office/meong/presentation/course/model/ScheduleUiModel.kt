package com.office.meong.presentation.course.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.LodgingType
import com.office.meong.core.model.place.PlaceType
import com.office.meong.data.course.model.AlternativePlace
import com.office.meong.data.course.model.CourseItem
import com.office.meong.data.favorite.model.FavoriteModel
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ScheduleUiModel(
    val id: String,
    val placeType: PlaceType,
    val placeName: String,
    val grade: String,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val distanceFromPrevKm: Double = 0.0,
    val thumbnailUrl: String? = null,
    val lodgingType: LodgingType? = null,
    val placeId: Long? = null
) {
    companion object {
        val DUMMY_SEARCHABLE_PLACES = persistentListOf(
            ScheduleUiModel(id = "search-1", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A"),
            ScheduleUiModel(id = "search-2", placeType = PlaceType.RESTAURANT, placeName = "댕댕이 맛집", grade = "A"),
            ScheduleUiModel(id = "search-3", placeType = PlaceType.SIGHTSEEING, placeName = "산책하기 좋은 공원", grade = "A"),
        )
    }
}

fun CourseItem.toUiModel(): ScheduleUiModel = ScheduleUiModel(
    id = id.toString(),
    placeType = PlaceType.from(placeType),
    placeName = placeName,
    grade = "",
    location = address,
    latitude = latitude,
    longitude = longitude,
    distanceFromPrevKm = distanceFromPrevKm,
    thumbnailUrl = thumbnailUrl,
    lodgingType = LodgingType.from(lodgingType),
    placeId = placeId,
)

fun AlternativePlace.toScheduleUiModel(): ScheduleUiModel = ScheduleUiModel(
    id = placeId.toString(),
    placeType = PlaceType.from(placeType),
    placeName = placeName,
    grade = grade,
    location = address,
    latitude = latitude,
    longitude = longitude,
    placeId = placeId,
)

fun FavoriteModel.toScheduleUiModel(): ScheduleUiModel = ScheduleUiModel(
    id = placeId.toString(),
    placeType = PlaceType.from(placeType),
    placeName = placeName,
    grade = grade,
    location = address,
    thumbnailUrl = thumbnailUrl,
    placeId = placeId,
)
