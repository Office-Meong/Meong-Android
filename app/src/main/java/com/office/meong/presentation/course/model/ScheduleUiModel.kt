package com.office.meong.presentation.course.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.LodgingType
import com.office.meong.core.model.place.PlaceType
import com.office.meong.data.course.model.AlternativePlace
import com.office.meong.data.course.model.CourseItem
import com.office.meong.data.favorite.model.FavoriteModel
import com.office.meong.data.place.model.PlaceSummary

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
)

/** id가 아직 서버에서 채워지지 않은 경우(방금 추가된 아이템) 다음 조회 전까지 임시 id를 사용한다. */
fun CourseItem.toUiModel(): ScheduleUiModel = ScheduleUiModel(
    id = id?.toString() ?: "pending-$placeId-$visitOrder",
    placeType = PlaceType.from(placeType),
    placeName = placeName,
    // TODO: CourseItemResponse에 실제 grade 필드가 내려오면 채우기(백엔드 확인 필요) — 현재는 항상 빈 값이라 펫-워크 칩이 뜨지 않음
    grade = "",
    location = address.orEmpty(),
    latitude = latitude,
    longitude = longitude,
    distanceFromPrevKm = distanceFromPrevKm ?: 0.0,
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

fun PlaceSummary.toScheduleUiModel(): ScheduleUiModel = ScheduleUiModel(
    id = id.toString(),
    placeType = PlaceType.from(placeType),
    placeName = name,
    grade = grade,
    location = address,
    thumbnailUrl = thumbnailUrl,
    placeId = id,
)
