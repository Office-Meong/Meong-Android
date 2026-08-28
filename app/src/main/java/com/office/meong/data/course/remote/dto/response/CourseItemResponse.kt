package com.office.meong.data.course.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 방금 추가된 아이템은 서버가 id를 채우기 전 응답을 내려줄 때가 있어 `id`가 null로 올 수 있다(백엔드 이슈).
 * 이 경우 다음 코스 조회 때 정상적으로 채워진다.
 * */
@Serializable
data class CourseItemResponse(
    @SerialName("id")
    val id: Long?,
    @SerialName("dayNumber")
    val dayNumber: Int,
    @SerialName("visitOrder")
    val visitOrder: Int,
    @SerialName("slotLabel")
    val slotLabel: String?,
    @SerialName("placeId")
    val placeId: Long,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("placeType")
    val placeType: String,
    @SerialName("address")
    val address: String?,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("startTime")
    val startTime: String?,
    @SerialName("endTime")
    val endTime: String?,
    @SerialName("distanceFromPrevKm")
    val distanceFromPrevKm: Double?,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerialName("lodgingType")
    val lodgingType: String? = null,
)