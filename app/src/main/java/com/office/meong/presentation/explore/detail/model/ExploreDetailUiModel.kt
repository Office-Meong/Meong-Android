package com.office.meong.presentation.explore.detail.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.AcmpyType
import com.office.meong.core.model.place.CongestionLevel
import com.office.meong.core.model.place.IndoorOutdoorType
import com.office.meong.core.model.place.PlaceType
import com.office.meong.data.place.model.PlaceDetail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

private const val DEFAULT_CONGESTION_TOOLTIP = "한국관광공사 정보를 바탕으로 하루 한 번 초기화돼요"
private const val NO_INFO = "정보 없음"

private val BULLET_DELIMITER_REGEX = Regex("[-*]")

private fun String.toBulletLines(): List<String> = split(BULLET_DELIMITER_REGEX)
    .map { it.trim() }
    .filter { it.isNotBlank() }

private fun String?.orNoInfo(): String = this.orEmpty().ifBlank { NO_INFO }

@Immutable
data class ExploreDetailUiModel(
    val placeType: PlaceType = PlaceType.OTHER,
    val title: String = "",
    val address: String = "",
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
    val grade: String = "",
    val isAllowed: String = "",
    val condition: String = "",
    val allowedSpace: String = "",
    val notice: String = "",
    val todayHours: String = "",
    val weeklyHours: ImmutableList<String> = persistentListOf(),
    val closedDays: String = "",
    val parkingInfo: String = "",
    val phoneNumber: String = "",
    val congestionLevel: String = "",
    val tooltipText: String = "",
    val accessibilityTags: ImmutableList<String> = persistentListOf()
) {
    companion object {
        val Dummy = ExploreDetailUiModel(
            placeType = PlaceType.ACCOMMODATION,
            title = "프렌즈애견펜션",
            address = "강원 강릉시 하남길 117-4",
            imageUrl = null,
            isFavorite = false,
            grade = "A",
            isAllowed = "동반 가능",
            condition = "소형견 가능, 목줄 필수",
            allowedSpace = "야외 테라스",
            notice = "이동 시 목줄을 착용해주세요",
            todayHours = "월 10:30 - 21:00",
            weeklyHours = persistentListOf(
                "화 10:30 - 21:00",
                "수 10:30 - 21:00",
                "목 10:30 - 21:00",
                "금 10:30 - 21:00",
                "토 10:30 - 21:00",
                "일 정기휴무 (매주 일요일)"
            ),
            closedDays = "매주 일요일",
            parkingInfo = "가능",
            phoneNumber = "033-000-0000",
            congestionLevel = "보통",
            tooltipText = "한국관광공사 정보를 바탕으로 하루 한 번 초기화돼요",
            accessibilityTags = persistentListOf("경사로 있음", "유모차 이동 가능")
        )
    }
}

fun PlaceDetail.toUiModel(): ExploreDetailUiModel = ExploreDetailUiModel(
    placeType = PlaceType.from(placeType),
    title = name,
    address = address,
    imageUrl = imageUrls.firstOrNull(),
    isFavorite = favorite,
    grade = score.grade,
    isAllowed = AcmpyType.from(petCondition.acmpyType).label,
    condition = petCondition.companionConditions?.toBulletLines()?.joinToString("\n").orNoInfo(),
    allowedSpace = (
        listOf(IndoorOutdoorType.from(operation.indoorOutdoorType).label) +
            (petCondition.availableFacilities?.toBulletLines() ?: emptyList())
        ).joinToString("\n"),
    notice = petCondition.cautions?.toBulletLines()?.joinToString("\n").orNoInfo(),
    todayHours = operation.operatingHours.orNoInfo(),
    weeklyHours = persistentListOf(),
    closedDays = operation.closedDays.orNoInfo(),
    parkingInfo = when (operation.parkingAvailable) {
        true -> "가능"
        false -> "불가능"
        null -> NO_INFO
    },
    phoneNumber = tel.orNoInfo(),
    congestionLevel = CongestionLevel.from(score.congestionLevel).label,
    tooltipText = DEFAULT_CONGESTION_TOOLTIP,
    accessibilityTags = buildList {
        if (accessibility.dataAvailable) {
            if (accessibility.hasRamp) add("경사로 있음")
            if (accessibility.strollerAccessible) add("유모차 이동 가능")
            if (accessibility.hasParking) add("주차 가능")
        }
    }.toImmutableList()
)
