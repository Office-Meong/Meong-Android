package com.office.meong.presentation.course.create.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class CalendarDayUiModel(
    val date: LocalDate,
    val dayType: CalendarDayType,
    val selectionState: CalendarDaySelectionState,
    val isToday: Boolean,
)

enum class CalendarDayType {
    PreviousMonth,
    CurrentMonth,
    NextMonth,
}

enum class CalendarDaySelectionState {
    None,
    SingleSelected,
    RangeStart,
    RangeMiddle,
    RangeEnd,
}

val CalendarDaySelectionState.isPrimarySelected: Boolean
    get() = this == CalendarDaySelectionState.SingleSelected ||
            this == CalendarDaySelectionState.RangeStart ||
            this == CalendarDaySelectionState.RangeEnd
