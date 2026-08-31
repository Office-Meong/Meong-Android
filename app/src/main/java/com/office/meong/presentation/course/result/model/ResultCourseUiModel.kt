package com.office.meong.presentation.course.result.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.common.util.formatWorkTimeRange
import com.office.meong.core.model.course.WorkFocusLevel
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.toUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

@Immutable
data class ResultCourseUiModel(
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val workStartTime: String,
    val workEndTime: String,
    val workFocusLevel: WorkFocusLevel,
    val dayItems: ImmutableMap<Int, ImmutableList<ScheduleUiModel>>,
    val dayReturnToAccommKm: ImmutableMap<Int, Double>,
) {
    val tripPeriod: String
        get() = formatTripPeriod(startDate, endDate)

    val workTimeRange: String
        get() = formatWorkTimeRange(workStartTime, workEndTime)
}

fun CourseDetail.toUiModel(): ResultCourseUiModel = ResultCourseUiModel(
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    totalDays = totalDays,
    workStartTime = workStartTime,
    workEndTime = workEndTime,
    workFocusLevel = WorkFocusLevel.from(workFocusLevel),
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
