package com.office.meong.presentation.course.create.model

enum class CreateCourseRangeInputType(
    val title: String,
    val startPlaceholder: String,
    val endPlaceholder: String,
    val hasInfoIcon: Boolean,
    val tooltipMessage: String? = null,
) {
    WORKCATION_PERIOD(
        title = "워케이션 기간",
        startPlaceholder = "YYYY.MM.DD",
        endPlaceholder = "YYYY.MM.DD",
        hasInfoIcon = false,
    ),
    WORK_TIME(
        title = "업무 시간",
        startPlaceholder = "오전 00:00",
        endPlaceholder = "오전 00:00",
        hasInfoIcon = true,
        tooltipMessage = "하루 중 업무에 집중하는 시간대를 알려주세요",
    ),
}
