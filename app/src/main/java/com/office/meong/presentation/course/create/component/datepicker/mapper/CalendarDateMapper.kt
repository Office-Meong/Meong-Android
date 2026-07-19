package com.office.meong.presentation.course.create.component.datepicker.mapper

import com.office.meong.presentation.course.create.component.datepicker.extension.firstDayOfMonth
import com.office.meong.presentation.course.create.component.datepicker.extension.sundayBasedDayOfWeekIndex
import com.office.meong.presentation.course.create.model.CalendarDaySelectionState
import com.office.meong.presentation.course.create.model.CalendarDayType
import com.office.meong.presentation.course.create.model.CalendarDayUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * [UI 상태 판단 로직] LocalDate → 캘린더 UI 모델 매핑 함수
 *
 * [CalendarDayType], [CalendarDaySelectionState] 등 UI 모델에 의존
 * LocalDate를 캘린더 그리드에 표시하기 위한 상태로 변환하는 책임만
 */

const val CalendarColumnCount = 7
private const val CalendarRowCount = 6

@OptIn(ExperimentalTime::class)
fun createCalendarWeeks(
    visibleMonth: LocalDate,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    today: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
): ImmutableList<ImmutableList<CalendarDayUiModel>> {
    val firstDayOfMonth = visibleMonth.firstDayOfMonth()
    val gridStartDate = firstDayOfMonth.minus(DatePeriod(days = firstDayOfMonth.sundayBasedDayOfWeekIndex))
    val weeksBuilder = persistentListOf<ImmutableList<CalendarDayUiModel>>().builder()

    repeat(CalendarRowCount) { rowIndex ->
        val weekBuilder = persistentListOf<CalendarDayUiModel>().builder()
        repeat(CalendarColumnCount) { colIndex ->
            val date = gridStartDate.plus(DatePeriod(days = rowIndex * CalendarColumnCount + colIndex))
            weekBuilder.add(
                CalendarDayUiModel(
                    date = date,
                    dayType = date.toDayType(visibleMonth),
                    selectionState = date.toSelectionState(
                        selectedStartDate = selectedStartDate,
                        selectedEndDate = selectedEndDate,
                    ),
                    isToday = date == today,
                )
            )
        }
        weeksBuilder.add(weekBuilder.build())
    }

    return weeksBuilder.build()
}

fun LocalDate.toDayType(visibleMonth: LocalDate): CalendarDayType = when {
    year == visibleMonth.year && month == visibleMonth.month -> CalendarDayType.CurrentMonth
    this < visibleMonth.firstDayOfMonth() -> CalendarDayType.PreviousMonth
    else -> CalendarDayType.NextMonth
}

fun LocalDate.toSelectionState(
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
): CalendarDaySelectionState {
    if (selectedStartDate == null) return CalendarDaySelectionState.None
    if (selectedEndDate == null || selectedStartDate == selectedEndDate) {
        return if (this == selectedStartDate) {
            CalendarDaySelectionState.SingleSelected
        } else {
            CalendarDaySelectionState.None
        }
    }

    val rangeStart = minOf(selectedStartDate, selectedEndDate)
    val rangeEnd = maxOf(selectedStartDate, selectedEndDate)

    return when {
        this == rangeStart -> CalendarDaySelectionState.RangeStart
        this == rangeEnd -> CalendarDaySelectionState.RangeEnd
        this in rangeStart..rangeEnd -> CalendarDaySelectionState.RangeMiddle
        else -> CalendarDaySelectionState.None
    }
}
