package com.office.meong.presentation.course.detail.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Immutable
data class DetailCourseUiModel(
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val dayItems: ImmutableMap<Int, ImmutableList<ScheduleUiModel>>,
    val dayReturnToAccommKm: ImmutableMap<Int, Double>,
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
    dayItems = dayItems
        .mapNotNull { (day, items) ->
            day.toIntOrNull()?.let { it to items.map { item -> item.toUiModel() }.toImmutableList() }
        }
        .toMap()
        .toImmutableMap(),
    dayReturnToAccommKm = dayReturnToAccommKm
        .mapNotNull { (day, km) -> day.toIntOrNull()?.let { it to km } }
        .toMap()
        .toImmutableMap(),
)