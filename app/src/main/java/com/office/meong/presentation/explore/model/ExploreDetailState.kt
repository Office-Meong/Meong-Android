package com.office.meong.presentation.explore.model


data class ExploreDetailUiState(
    val typeText: String = "",
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
    val weeklyHours: List<String> = emptyList(),
    val closedDays: String = "",
    val parkingInfo: String = "",
    val phoneNumber: String = "",
    val congestionLevel: String = "",
    val tooltipText: String = "",
    val accessibilityTags: List<String> = emptyList()
) {
    companion object {
        val Dummy = ExploreDetailUiState(
            typeText = "숙소",
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
            weeklyHours = listOf(
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
            accessibilityTags = listOf("경사로 있음", "유모차 이동 가능")
        )
    }
}
