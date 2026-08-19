package com.office.meong.presentation.course.result.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.toUiModel

@Immutable
data class ResultCourseUiModel(
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val dayItems: Map<Int, List<ScheduleUiModel>>,
) {
    val tripPeriod: String
        get() = formatTripPeriod(startDate, endDate)

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
    dayItems = dayItems.mapKeys { (day, _) -> day.toInt() }
        .mapValues { (_, items) -> items.map { it.toUiModel() } },
)
