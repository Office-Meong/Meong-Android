package com.office.meong.presentation.course.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.course.create.component.CreateCourseUserInfoHolder
import com.office.meong.presentation.course.create.component.datepicker.CreateCourseDatePicker
import com.office.meong.presentation.course.create.component.setting.CreateCourseChipSelector
import com.office.meong.presentation.course.create.component.setting.CreateCourseDropdownSelector
import com.office.meong.presentation.course.create.component.setting.CreateCourseRangeInput
import com.office.meong.presentation.course.create.component.timepicker.CreateCourseTimePicker
import com.office.meong.presentation.course.create.component.timepicker.extension.formatCourseTime
import com.office.meong.presentation.course.create.model.CreateCourseRangeInputType
import com.office.meong.presentation.course.create.model.WorkTimeInput
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun CreateCourseRoute(

) {
    CreateCourseScreen()
}

@OptIn(ExperimentalTime::class)
@Composable
private fun CreateCourseScreen(

) {
    val scrollState = rememberScrollState()
    var isWorkcationStyleExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    var visibleMonth by remember { mutableStateOf(LocalDate(today.year, today.month, 1)) }
    var selectedStartDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedEndDate by remember { mutableStateOf<LocalDate?>(null) }
    var showTimePicker by remember { mutableStateOf(false) }
    var activeWorkTimeInput by remember { mutableStateOf(WorkTimeInput.Start) }
    var selectedStartWorkTime by remember { mutableStateOf<LocalTime?>(null) }
    var selectedEndWorkTime by remember { mutableStateOf<LocalTime?>(null) }

    LaunchedEffect(isWorkcationStyleExpanded, scrollState.maxValue) {
        if (isWorkcationStyleExpanded) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MeongTheme.colors.white)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        MeongTheme.typography.title.title20Sb.copy(
                            color = MeongTheme.colors.primary
                        ).toSpanStyle()
                    ) {
                        append("몽몽이")
                    }

                    append("와 함께하는\n워케이션 코스를 추천해 드려요")
                },
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.gray900,
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        CreateCourseUserInfoHolder(
            url = ""
        )

        Spacer(modifier = Modifier.height(24.dp))

        CreateCourseRangeInput(
            type = CreateCourseRangeInputType.WORKCATION_PERIOD,
            onClick = {
                showDatePicker = true
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseChipSelector(
            title = "지역 선택",
            chips = persistentListOf("강릉", "춘천", "원주"),
            selectedChips = persistentListOf("강릉"),
            onChipClick = {

            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseRangeInput(
            type = CreateCourseRangeInputType.WORK_TIME,
            startText = selectedStartWorkTime?.formatCourseTime(),
            endText = selectedEndWorkTime?.formatCourseTime(),
            onClick = {},
            onStartClick = {
                activeWorkTimeInput = WorkTimeInput.Start
                showTimePicker = true
            },
            onEndClick = {
                activeWorkTimeInput = WorkTimeInput.End
                showTimePicker = true
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        CreateCourseChipSelector(
            title = "숙소 유형",
            chips = persistentListOf("펜션", "민박", "캠핑장", "글램핑장", "호텔", "카라반"),
            selectedChips = persistentListOf("캠핑장"),
            onChipClick = {

            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseDropdownSelector(
            title = "워케이션 스타일",
            placeholder = "선호하는 워케이션 스타일을 선택해주세요",
            options = persistentListOf(
                "업무는 최소한으로, 여행을 마음껏 즐길래요",
                "일과 여행, 적당히 균형을 맞출래요",
                "일에 몰입할 수 있는 환경이 필요해요",
            ),
            selectedOption = null,
            isExpanded = isWorkcationStyleExpanded,
            onExpandedChange = { isWorkcationStyleExpanded = it },
            onOptionClick = {},
        )

        Spacer(modifier = Modifier.height(32.dp))

        MeongButton(
            text = "코스 생성하기",
            isEnabled = false,
            onClick = {}
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showDatePicker) {
        CreateCourseDatePicker(
            visibleMonth = visibleMonth,
            selectedStartDate = selectedStartDate,
            selectedEndDate = selectedEndDate,
            onDismiss = { showDatePicker = false },
            onPreviousMonthClick = { visibleMonth = visibleMonth.minus(1, DateTimeUnit.MONTH) },
            onNextMonthClick = { visibleMonth = visibleMonth.plus(1, DateTimeUnit.MONTH) },
            onDateClick = { date ->
                when {
                    selectedStartDate == null || selectedEndDate != null -> {
                        selectedStartDate = date
                        selectedEndDate = null
                    }
                    date >= selectedStartDate!! -> selectedEndDate = date
                    else -> selectedStartDate = date
                }
            },
            onSaveClick = { showDatePicker = false },
        )
    }

    if (showTimePicker) {
        CreateCourseTimePicker(
            initialTime = when (activeWorkTimeInput) {
                WorkTimeInput.Start -> selectedStartWorkTime
                WorkTimeInput.End -> selectedEndWorkTime
            },
            onDismiss = { showTimePicker = false },
            onSaveClick = { time ->
                when (activeWorkTimeInput) {
                    WorkTimeInput.Start -> selectedStartWorkTime = time
                    WorkTimeInput.End -> selectedEndWorkTime = time
                }
                showTimePicker = false
            },
        )
    }
}

@Preview
@Composable
private fun CreateCourseScreenPreview() {
    MeongTheme {
        CreateCourseScreen()
    }
}
