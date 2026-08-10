package com.office.meong.presentation.home.model

import androidx.compose.runtime.Immutable
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.data.course.model.CourseSummary
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
)

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
