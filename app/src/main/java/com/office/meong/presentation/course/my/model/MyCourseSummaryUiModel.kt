package com.office.meong.presentation.course.my.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.common.util.formatTripPeriod
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseSummary
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MyCourseSummaryUiModel(
    val id: Long,
    val name: String,
    val region: Region,
    val startDate: String,
    val endDate: String,
    val totalDays: Int,
    val averageGrade: String,
    val places: ImmutableList<MyCoursePlaceCategory>,
    val totalPlaceCount: Int,
) {
    val tripPeriod: String
        get() = formatTripPeriod(startDate, endDate)
}

fun CourseSummary.toUiModel() = MyCourseSummaryUiModel(
    id = id,
    name = name,
    region = region,
    startDate = startDate,
    endDate = endDate,
    totalDays = totalDays,
    averageGrade = averageGrade,
    places = persistentListOf(
        MyCoursePlaceCategory(PlaceType.WORKSPACE, workPlaceCount),
        MyCoursePlaceCategory(PlaceType.RESTAURANT, foodCount),
        MyCoursePlaceCategory(PlaceType.SIGHTSEEING, tourWalkCount),
        MyCoursePlaceCategory(PlaceType.OTHER, otherCount),
    ),
    totalPlaceCount = totalPlaceCount
)
