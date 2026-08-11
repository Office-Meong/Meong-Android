package com.office.meong.presentation.home.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseSummary
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Immutable
data class HomeCourseSummaryUiModel(
    val id: Long,
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val averageGrade: String,
    val places: ImmutableList<HomePlaceCategory>,
    val totalPlaceCount: Int,
) {
    val tripPeriod: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = formatTripPeriod(startDate, endDate)
}

fun CourseSummary.toUiModel() = HomeCourseSummaryUiModel(
    id = id,
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    totalDays = totalDays,
    averageGrade = averageGrade,
    places = persistentListOf(
        HomePlaceCategory(PlaceType.WORKSPACE, workPlaceCount),
        HomePlaceCategory(PlaceType.RESTAURANT, foodCount),
        HomePlaceCategory(PlaceType.SIGHTSEEING, tourWalkCount),
        HomePlaceCategory(PlaceType.OTHER, otherCount),
    ),
    totalPlaceCount = totalPlaceCount
)

@RequiresApi(Build.VERSION_CODES.O)
private val TRIP_DATE_DISPLAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.M.d")

// startDate/endDate("yyyy-MM-dd")를 "N박 M일 (yyyy.M.d - yyyy.M.d)" 형태로 변환
@RequiresApi(Build.VERSION_CODES.O)
fun formatTripPeriod(startDate: String, endDate: String): String {
    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)
    val days = ChronoUnit.DAYS.between(start, end).toInt() + 1
    val nights = days - 1

    return "${nights}박 ${days}일 (${start.format(TRIP_DATE_DISPLAY_FORMATTER)} - ${end.format(TRIP_DATE_DISPLAY_FORMATTER)})"
}
