package com.office.meong.presentation.course.create.component.datepicker.extension

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

/**
 * [순수 계산 로직] LocalDate 날짜 연산 확장 함수
 *
 * UI 모델에 대한 의존성이 없는 순수 날짜 계산만 포함
 * 캘린더 UI 상태와 무관하게 어느 컨텍스트에서도 재사용 가능
 */
fun LocalDate.firstDayOfMonth(): LocalDate = LocalDate(year, month, 1)

val LocalDate.sundayBasedDayOfWeekIndex: Int
    get() = when (dayOfWeek) {
        DayOfWeek.SUNDAY -> 0
        DayOfWeek.MONDAY -> 1
        DayOfWeek.TUESDAY -> 2
        DayOfWeek.WEDNESDAY -> 3
        DayOfWeek.THURSDAY -> 4
        DayOfWeek.FRIDAY -> 5
        DayOfWeek.SATURDAY -> 6
    }