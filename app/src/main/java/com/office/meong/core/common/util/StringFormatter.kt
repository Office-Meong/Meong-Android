package com.office.meong.core.common.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus

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

// date("yyyy-MM-dd")를 "M.d" 형태로 변환
fun formatShortTripDate(date: String): String = LocalDate.parse(date).toTripDisplayShortFormat()

// startDate("yyyy-MM-dd") 기준 dayNumber(1부터 시작) 일차의 날짜를 "M.d" 형태로 변환
fun formatDayDate(startDate: String, dayNumber: Int): String =
    LocalDate.parse(startDate).plus(dayNumber - 1, DateTimeUnit.DAY).toTripDisplayShortFormat()

private fun LocalDate.toTripDisplayShortFormat(): String = "$monthNumber.$dayOfMonth"

// 거리(km)를 소수점 1자리 문자열로 변환
fun formatDistanceKm(distanceKm: Double): String = "%.1f".format(distanceKm)
