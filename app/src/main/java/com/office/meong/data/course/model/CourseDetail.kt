package com.office.meong.data.course.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.course.remote.dto.response.CourseResponse
import com.office.meong.data.course.remote.dto.response.CourseItemResponse

data class CourseItem(
    val id: Long,
    val dayNumber: Int,
    val visitOrder: Int,
    val slotLabel: String,
    val placeId: Long,
    val placeName: String,
    val placeType: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startTime: String,
    val endTime: String,
    val distanceFromPrevKm: Double,
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
    dayItems = dayItems.mapValues { (_, items) -> items.map { it.toModel() } },
    createdAt = createdAt,
)