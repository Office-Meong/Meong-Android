package com.office.meong.data.course.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 방금 추가된 아이템은 서버가 id를 채우기 전 응답을 내려줄 때가 있어 `id`가 null로 올 수 있다(백엔드 이슈).
 * 이 경우 다음 코스 조회 때 정상적으로 채워진다.
 *
 * 좌표·시간·주소 등은 원본 데이터가 없는 장소(관광공사 임포트 등)에서 서버가 null로 내려주므로
 * 파싱이 통째로 깨지지 않도록 전부 기본값을 둔다.
 * */
@Serializable
data class CourseItemResponse(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("dayNumber")
    val dayNumber: Int = 0,
    @SerialName("visitOrder")
    val visitOrder: Int = 0,
    @SerialName("slotLabel")
    val slotLabel: String? = null,
    @SerialName("placeId")
    val placeId: Long = 0L,
    @SerialName("placeName")
    val placeName: String = "",
    @SerialName("placeType")
    val placeType: String = "",
    @SerialName("address")
    val address: String? = null,
    @SerialName("latitude")
    val latitude: Double = 0.0,
    @SerialName("longitude")
    val longitude: Double = 0.0,
    @SerialName("startTime")
    val startTime: String? = null,
    @SerialName("endTime")
    val endTime: String? = null,
    @SerialName("distanceFromPrevKm")
    val distanceFromPrevKm: Double? = null,
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerialName("lodgingType")
    val lodgingType: String? = null,
    @SerialName("grade")
    val grade: String? = null,
)
