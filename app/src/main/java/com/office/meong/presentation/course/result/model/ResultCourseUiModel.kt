package com.office.meong.presentation.course.result.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.common.util.formatWorkTimeRange
import com.office.meong.core.model.course.WorkFocusLevel
import com.office.meong.core.model.place.PlaceType
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
) {
    val tripPeriod: String
        get() = formatTripPeriod(startDate, endDate)

    val workTimeRange: String
        get() = formatWorkTimeRange(workStartTime, workEndTime)

    /** 코스 전체 일정 중 숙소(STAY)로 취급되는 아이템. dayItems에서 매번 다시 계산되므로 항상 최신 상태 */
    val accommodation: ScheduleUiModel?
        get() = dayItems.values.flatten().firstOrNull { it.placeType == PlaceType.ACCOMMODATION }
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
    dayItems = dayItems.mapKeys { (day, _) -> day.toInt() }
        .mapValues { (_, items) -> items.map { it.toUiModel() }.toImmutableList() }
        .toImmutableMap(),
)
