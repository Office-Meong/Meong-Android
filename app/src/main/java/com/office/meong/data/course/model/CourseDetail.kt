package com.office.meong.data.course.model

import com.office.meong.core.common.util.calculateStraightLineDistanceKm
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.remote.dto.response.CourseResponse
import com.office.meong.data.course.remote.dto.response.CourseItemResponse

data class CourseItem(
    val id: Long?,
    val dayNumber: Int,
    val visitOrder: Int,
    val slotLabel: String?,
    val placeId: Long,
    val placeName: String,
    val placeType: String,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val startTime: String?,
    val endTime: String?,
    val distanceFromPrevKm: Double?,
    val thumbnailUrl: String?,
    val lodgingType: String?,
    val grade: String?,
)

fun CourseItemResponse.toModel(): CourseItem = CourseItem(
    id = id,
    dayNumber = dayNumber,
    visitOrder = visitOrder,
    slotLabel = slotLabel,
    placeId = placeId,
    placeName = placeName,
    placeType = placeType,
    address = address,
    latitude = latitude,
    longitude = longitude,
    startTime = startTime,
    endTime = endTime,
    distanceFromPrevKm = distanceFromPrevKm,
    thumbnailUrl = thumbnailUrl,
    lodgingType = lodgingType,
    grade = grade,
)

data class CourseDetail(
    val id: Long,
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val workStartTime: String,
    val workEndTime: String,
    val workFocusLevel: String,
    val totalDays: Int,
    val dayItems: Map<String, List<CourseItem>>,
    val dayReturnToAccommKm: Map<String, Double>,
    val createdAt: String,
)

fun CourseResponse.toModel(): CourseDetail = CourseDetail(
    id = id,
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    workStartTime = workStartTime,
    workEndTime = workEndTime,
    workFocusLevel = workFocusLevel,
    totalDays = totalDays,
    dayItems = dayItems
        .mapValues { (_, items) -> items.map { it.toModel() }.sortedBy { it.visitOrder } }
        .withEstimatedDistances(),
    dayReturnToAccommKm = dayReturnToAccommKm,
    createdAt = createdAt,
)

private fun Map<String, List<CourseItem>>.accommodationForDay(dayKey: String): CourseItem? =
    this[dayKey]?.firstOrNull { PlaceType.from(it.placeType) == PlaceType.ACCOMMODATION }
        ?: values.flatten().firstOrNull { PlaceType.from(it.placeType) == PlaceType.ACCOMMODATION }

/**
 * 서버가 distanceFromPrevKm를 못 채워준(null) 항목의 거리를 좌표 기반 직선거리로 추정해서 채운다.
 * 하루의 첫 장소는 그날의 숙소 좌표 기준, 그 외에는 같은 날 바로 이전 장소 좌표 기준으로 계산한다.
 */
private fun Map<String, List<CourseItem>>.withEstimatedDistances(): Map<String, List<CourseItem>> =
    mapValues { (day, items) ->
        val accommodation = accommodationForDay(day)
        items.mapIndexed { index, item ->
            if (item.distanceFromPrevKm != null) return@mapIndexed item

            val previous = if (index > 0) items[index - 1] else accommodation
            val estimatedKm = previous?.let {
                calculateStraightLineDistanceKm(it.latitude, it.longitude, item.latitude, item.longitude)
            }
            estimatedKm?.let { item.copy(distanceFromPrevKm = it) } ?: item
        }
    }
