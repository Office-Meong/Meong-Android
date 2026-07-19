package com.office.meong.presentation.course.create.component.datepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.bottomsheet.MeongDragHandle
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.course.create.component.datepicker.mapper.createCalendarWeeks
import com.office.meong.presentation.course.create.model.CalendarDaySelectionState
import com.office.meong.presentation.course.create.model.CalendarDayType
import com.office.meong.presentation.course.create.model.CalendarDayUiModel
import com.office.meong.presentation.course.create.model.isPrimarySelected
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.LocalDate

private val CalendarWeekLabels = listOf("일", "월", "화", "수", "목", "금", "토")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseDatePicker(
    visibleMonth: LocalDate,
    selectedStartDate: LocalDate?,
    selectedEndDate: LocalDate?,
    onDismiss: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val calendarWeeks = remember(visibleMonth, selectedStartDate, selectedEndDate) {
        createCalendarWeeks(
            visibleMonth = visibleMonth,
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
        )
    }

    MeongBottomSheet(
        onDismiss = onDismiss,
        dragHandle = { MeongDragHandle(bottomPadding = 20.dp) }
    ) {
        CreateCourseDatePickerContent(
            visibleMonth = visibleMonth,
            calendarWeeks = calendarWeeks,
            onPreviousMonthClick = onPreviousMonthClick,
            onNextMonthClick = onNextMonthClick,
            onDateClick = onDateClick,
            onSaveClick = onSaveClick,
            modifier = modifier,
        )
    }
}

@Composable
fun CreateCourseDatePickerContent(
    visibleMonth: LocalDate,
    calendarWeeks: ImmutableList<ImmutableList<CalendarDayUiModel>>,
    onPreviousMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDateClick: (LocalDate) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = "이전 달",
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(20.dp)
                    .noRippleClickable(onPreviousMonthClick),
                tint = MeongTheme.colors.gray900,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "${visibleMonth.year}년 ${visibleMonth.monthNumber}월",
                style = MeongTheme.typography.title.title16Sb,
                color = MeongTheme.colors.gray900,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = "다음 달",
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .size(20.dp)
                    .noRippleClickable(onNextMonthClick),
                tint = MeongTheme.colors.gray900,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        CalendarWeekHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(8.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            val cellWidth = maxWidth / 7
            Column {
                calendarWeeks.forEach { weekDays ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        weekDays.forEach { day ->
                            CreateCourseDatePickerItem(
                                day = day,
                                cellWidth = cellWidth,
                                onDateClick = onDateClick,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }
            }
        }

        MeongButton(
            text = "저장하기",
            onClick = onSaveClick,
            modifier = Modifier
                .padding(20.dp)
        )
    }
}

@Composable
private fun CreateCourseDatePickerItem(
    day: CalendarDayUiModel,
    cellWidth: Dp,
    onDateClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = MeongTheme.colors
    val rangeBackground = when (day.selectionState) {
        CalendarDaySelectionState.RangeStart,
        CalendarDaySelectionState.RangeMiddle,
        CalendarDaySelectionState.RangeEnd -> colors.primaryLight
        CalendarDaySelectionState.None,
        CalendarDaySelectionState.SingleSelected -> Color.Transparent
    }
    val primaryBackground = when (day.selectionState) {
        CalendarDaySelectionState.None -> Color.Transparent
        CalendarDaySelectionState.SingleSelected,
        CalendarDaySelectionState.RangeStart,
        CalendarDaySelectionState.RangeEnd -> colors.primary
        CalendarDaySelectionState.RangeMiddle -> Color.Transparent
    }
    val textColor = when {
        day.selectionState.isPrimarySelected -> colors.white
        day.dayType == CalendarDayType.CurrentMonth -> colors.black
        else -> colors.gray400
    }
    val todayIndicatorColor = if (day.selectionState.isPrimarySelected) {
        colors.white
    } else {
        colors.primary
    }
    val rangeShape = day.selectionState.toRangeShape()
    val primaryShape = if (day.selectionState.isPrimarySelected) CircleShape else RectangleShape
    val selectedCircleSize = 32.dp
    val rangePadding = 3.dp
    val rangeHeight = selectedCircleSize + rangePadding * 2
    val edgeRangeWidth = (cellWidth + selectedCircleSize) / 2 + rangePadding

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .noRippleClickable { onDateClick(day.date) },
        contentAlignment = Alignment.Center,
    ) {
        val rangeModifier = when (day.selectionState) {
            CalendarDaySelectionState.RangeStart -> Modifier
                .align(Alignment.CenterEnd)
                .width(edgeRangeWidth)
            CalendarDaySelectionState.RangeEnd -> Modifier
                .align(Alignment.CenterStart)
                .width(edgeRangeWidth)
            else -> Modifier.fillMaxWidth()
        }

        Box(
            modifier = rangeModifier
                .height(rangeHeight)
                .clip(rangeShape)
                .background(rangeBackground),
        )

        Box(
            modifier = Modifier
                .size(selectedCircleSize)
                .clip(primaryShape)
                .background(primaryBackground),
            contentAlignment = Alignment.Center,
        ) {
            if (day.isToday) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = day.date.day.toString(),
                        style = MeongTheme.typography.body.body16M,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(todayIndicatorColor),
                    )
                }
            } else {
                Text(
                    text = day.date.day.toString(),
                    style = MeongTheme.typography.body.body16M,
                    color = textColor,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun CalendarWeekHeader(
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        CalendarWeekLabels.forEach { label ->
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MeongTheme.typography.label.label12Sb,
                color = MeongTheme.colors.gray500,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun CalendarDaySelectionState.toRangeShape(): Shape = when (this) {
    CalendarDaySelectionState.None -> RectangleShape
    CalendarDaySelectionState.SingleSelected -> RectangleShape
    CalendarDaySelectionState.RangeStart -> RoundedCornerShape(
        topStart = 999.dp,
        bottomStart = 999.dp,
    )
    CalendarDaySelectionState.RangeMiddle -> RectangleShape
    CalendarDaySelectionState.RangeEnd -> RoundedCornerShape(
        topEnd = 999.dp,
        bottomEnd = 999.dp,
    )
}

@Preview
@Composable
private fun CreateCourseDatePickerPreview() {
    MeongTheme {
        val visibleMonth = LocalDate(2026, 8, 1)
        val selectedStartDate = LocalDate(2026, 8, 10)
        val selectedEndDate = LocalDate(2026, 8, 19)

        val calendarWeeks = remember(visibleMonth, selectedStartDate, selectedEndDate) {
            createCalendarWeeks(
                visibleMonth = visibleMonth,
                selectedStartDate = selectedStartDate,
                selectedEndDate = selectedEndDate,
            )
        }

        CreateCourseDatePickerContent(
            visibleMonth = visibleMonth,
            calendarWeeks = calendarWeeks,
            onPreviousMonthClick = {},
            onNextMonthClick = {},
            onDateClick = {},
            onSaveClick = {},
        )
    }
}
