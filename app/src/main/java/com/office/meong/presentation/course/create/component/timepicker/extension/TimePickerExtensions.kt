package com.office.meong.presentation.course.create.component.timepicker.extension

import kotlinx.datetime.LocalTime

fun LocalTime.formatCourseTime(): String {
    val meridiem = if (hour < 12) "오전" else "오후"
    val twelveHour = hour % 12
    val displayHour = if (twelveHour == 0) 12 else twelveHour
    return "$meridiem $displayHour:${minute.toString().padStart(2, '0')}"
}

fun LocalTime.toTwelveHour(): Int {
    val hour = hour % 12
    return if (hour == 0) 12 else hour
}

fun Int.toTwentyFourHour(isAfternoon: Boolean): Int = when {
    isAfternoon -> if (this == 12) 12 else this + 12
    else -> if (this == 12) 0 else this
}