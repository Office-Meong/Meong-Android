package com.office.meong.presentation.course.detail.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseDetail

@Immutable
data class DetailCourseUiModel(
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val dayItems: Map<Int, List<ScheduleUiModel>>,
) {
    val tripPeriod: String
        get() = formatTripPeriod(startDate, endDate)
}

fun CourseDetail.toUiModel(): DetailCourseUiModel = DetailCourseUiModel(
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    totalDays = totalDays,
    dayItems = dayItems.mapKeys { (day, _) -> day.toInt() }
        .mapValues { (_, items) -> items.map { it.toUiModel() } },
)