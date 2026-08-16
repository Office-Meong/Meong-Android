package com.office.meong.presentation.course.create.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.office.meong.presentation.course.create.model.WorkTimeInput
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Stable
class CreateCourseUiState(
    initialVisibleMonth: LocalDate,
) {
    var isWorkcationStyleExpanded by mutableStateOf(false)
        private set

    var showDatePicker by mutableStateOf(false)
        private set
    var visibleMonth by mutableStateOf(initialVisibleMonth)
        private set

    var showTimePicker by mutableStateOf(false)
        private set
    var activeWorkTimeInput by mutableStateOf(WorkTimeInput.Start)
        private set

    fun changeWorkcationStyleExpanded(expanded: Boolean) {
        isWorkcationStyleExpanded = expanded
    }

    fun openDatePicker() {
        showDatePicker = true
    }

    fun closeDatePicker() {
        showDatePicker = false
    }

    fun showPreviousMonth() {
        visibleMonth = visibleMonth.minus(1, DateTimeUnit.MONTH)
    }

    fun showNextMonth() {
        visibleMonth = visibleMonth.plus(1, DateTimeUnit.MONTH)
    }

    fun openTimePicker(input: WorkTimeInput) {
        activeWorkTimeInput = input
        showTimePicker = true
    }

    fun closeTimePicker() {
        showTimePicker = false
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun rememberCreateCourseUiState(): CreateCourseUiState {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    return remember {
        CreateCourseUiState(initialVisibleMonth = LocalDate(today.year, today.month, 1))
    }
}
