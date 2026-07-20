package com.office.meong.presentation.course.create.component.timepicker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.common.extension.disableNestedScroll
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.bottomsheet.MeongDragHandle
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.course.create.component.timepicker.extension.toTwelveHour
import com.office.meong.presentation.course.create.component.timepicker.extension.toTwentyFourHour
import com.sonms.wheelpicker.VerticalWheelPicker
import com.sonms.wheelpicker.state.rememberWheelPickerState
import com.sonms.wheelpicker.style.WheelPickerDefaults
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalTime

private val MeridiemItems = listOf("오전", "오후")
private val HourItems = (1..12).toList()
private val MinuteItems = (0..55 step 5).toList()

private const val MorningIndex = 0
private const val AfternoonIndex = 1
private const val MinuteStep = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCourseTimePicker(
    initialTime: LocalTime?,
    onDismiss: () -> Unit,
    onSaveClick: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    MeongBottomSheet(
        onDismiss = onDismiss,
        dragHandle = { MeongDragHandle(bottomPadding = 38.dp) },
    ) {
        CreateCourseTimePickerContent(
            initialTime = initialTime,
            onSaveClick = onSaveClick,
            modifier = modifier,
        )
    }
}

@Composable
fun CreateCourseTimePickerContent(
    initialTime: LocalTime?,
    onSaveClick: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pickerInitialTime = remember(initialTime) { initialTime ?: LocalTime(hour = 8, minute = 0) }
    val initialMeridiemIndex = if (pickerInitialTime.hour < 12) MorningIndex else AfternoonIndex
    val initialHourIndex = pickerInitialTime.toTwelveHour() - 1
    val initialMinuteIndex = (pickerInitialTime.minute / MinuteStep).coerceIn(MinuteItems.indices)

    var selectedMeridiemIndex by remember(pickerInitialTime) { mutableIntStateOf(initialMeridiemIndex) }
    var selectedHour by remember(pickerInitialTime) { mutableIntStateOf(pickerInitialTime.toTwelveHour()) }
    var selectedMinute by remember(pickerInitialTime) { mutableIntStateOf(MinuteItems[initialMinuteIndex]) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            ),
    ) {
        TimePickerWheelRow(
            initialMeridiemIndex = initialMeridiemIndex,
            initialHourIndex = initialHourIndex,
            initialMinuteIndex = initialMinuteIndex,
            onMeridiemSelected = { selectedMeridiemIndex = it },
            onHourSelected = { selectedHour = it },
            onMinuteSelected = { selectedMinute = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(22.dp))

        MeongButton(
            text = "저장하기",
            onClick = {
                onSaveClick(
                    LocalTime(
                        hour = selectedHour.toTwentyFourHour(selectedMeridiemIndex == AfternoonIndex),
                        minute = selectedMinute,
                    ),
                )
            },
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun TimePickerWheelRow(
    initialMeridiemIndex: Int,
    initialHourIndex: Int,
    initialMinuteIndex: Int,
    onMeridiemSelected: (Int) -> Unit,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemHeight = 46.dp
    val wheelStyle = WheelPickerDefaults.style(
        selector = WheelPickerDefaults.selectorStyle(
            background = Color.Transparent,
            showDivider = false,
        ),
        transform = WheelPickerDefaults.transformStyle(
            rotationEnabled = false,
            scaleEnabled = false,
            alphaEnabled = false,
        ),
    )

    val meridiemState = rememberWheelPickerState(initialIndex = initialMeridiemIndex)
    val hourState = rememberWheelPickerState(initialIndex = initialHourIndex)
    val minuteState = rememberWheelPickerState(initialIndex = initialMinuteIndex)
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.height(itemHeight * 3),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = MeongTheme.colors.primaryBg,
                    shape = RoundedCornerShape(10.dp),
                ),
        )

        Row(
            modifier = Modifier.fillMaxWidth(0.60f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VerticalWheelPicker(
                items = MeridiemItems,
                state = meridiemState,
                itemHeight = itemHeight,
                visibleItemCount = 3,
                infinite = false,
                style = wheelStyle,
                onItemSelected = { index, _ -> onMeridiemSelected(index) },
                modifier = Modifier.weight(1f),
            ) { meridiem, isSelected ->
                TimePickerWheelText(
                    text = meridiem,
                    isSelected = isSelected,
                    onClick = {
                        scope.launch {
                            meridiemState.animateScrollToIndex(MeridiemItems.indexOf(meridiem))
                        }
                    },
                )
            }

            VerticalWheelPicker(
                items = HourItems,
                state = hourState,
                itemHeight = itemHeight,
                visibleItemCount = 3,
                style = wheelStyle,
                onItemSelected = { _, hour -> onHourSelected(hour) },
                modifier = Modifier.weight(1f),
            ) { hour, isSelected ->
                TimePickerWheelText(
                    text = hour.toString(),
                    isSelected = isSelected,
                    onClick = {
                        scope.launch {
                            hourState.animateScrollToIndex(HourItems.indexOf(hour))
                        }
                    },
                )
            }

            Text(
                text = ":",
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.35f),
            )

            VerticalWheelPicker(
                items = MinuteItems,
                state = minuteState,
                itemHeight = itemHeight,
                visibleItemCount = 3,
                style = wheelStyle,
                onItemSelected = { _, minute -> onMinuteSelected(minute) },
                modifier = Modifier.weight(1f),
            ) { minute, isSelected ->
                TimePickerWheelText(
                    text = minute.toString().padStart(2, '0'),
                    isSelected = isSelected,
                    onClick = {
                        scope.launch {
                            minuteState.animateScrollToIndex(MinuteItems.indexOf(minute))
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun TimePickerWheelText(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MeongTheme.typography.label.label14Sb,
        color = if (isSelected) MeongTheme.colors.primary else MeongTheme.colors.gray400,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
    )
}

@Preview
@Composable
private fun CreateCourseTimePickerPreview() {
    MeongTheme {
        CreateCourseTimePickerContent(
            initialTime = LocalTime(hour = 8, minute = 0),
            onSaveClick = {},
        )
    }
}
