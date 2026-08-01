package com.office.meong.presentation.course.detail.action

import androidx.compose.runtime.Stable

@Stable
interface DetailCourseEditActions {
    val title: TitleEditActions
    val accommodation: AccommodationEditActions
    val schedule: ScheduleEditActions
}

@Stable
interface TitleEditActions {
    fun onClickEdit()
    fun onClickComplete()
}

@Stable
interface AccommodationEditActions {
    fun onClickEdit()
    fun onClickComplete()
}

@Stable
interface ScheduleEditActions {
    fun onClickEdit()
    fun onClickComplete()
}
