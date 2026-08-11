package com.office.meong.core.common.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil

private fun LocalDate.toTripDisplayFormat(): String = "$year.$monthNumber.$dayOfMonth"

// startDate/endDate("yyyy-MM-dd")를 "N박 M일 (yyyy.M.d - yyyy.M.d)" 형태로 변환
fun formatTripPeriod(startDate: String, endDate: String): String {
    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)
    val days = start.daysUntil(end) + 1
    val nights = days - 1

    return "${nights}박 ${days}일 (${start.toTripDisplayFormat()} - ${end.toTripDisplayFormat()})"
}

// startDate/endDate("yyyy-MM-dd")를 "N박 M일" 형태로 변환
fun formatTripDuration(startDate: String, endDate: String): String {
    val start = LocalDate.parse(startDate)
    val end = LocalDate.parse(endDate)
    val days = start.daysUntil(end) + 1
    val nights = days - 1

    return "${nights}박 ${days}일"
}
