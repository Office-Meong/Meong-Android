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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.course.WorkFocusLevel
import com.office.meong.core.model.region.Region
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.course.create.component.datepicker.CreateCourseDatePicker
import com.office.meong.presentation.course.create.component.datepicker.extension.formatCourseDate
import com.office.meong.presentation.course.create.component.setting.CreateCourseChipSelector
import com.office.meong.presentation.course.create.component.setting.CreateCourseDropdownSelector
import com.office.meong.presentation.course.create.component.setting.CreateCourseRangeInput
import com.office.meong.presentation.course.create.component.timepicker.CreateCourseTimePicker
import com.office.meong.presentation.course.create.component.timepicker.extension.formatCourseTime
import com.office.meong.presentation.course.create.model.CreateCourseRangeInputType
import com.office.meong.presentation.course.create.model.WorkTimeInput
import com.office.meong.presentation.course.create.state.rememberCreateCourseUiState
import com.office.meong.presentation.sharedcomponent.PetProfileCard
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

@Composable
fun CreateCourseRoute(
    viewModel: CreateCourseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is CreateCourseSideEffect.ShowToast -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    CreateCourseScreen(
        state = state,
        onRetryPetInfo = viewModel::retryPetInfo,
        onSelectRegion = viewModel::selectRegion,
        onSelectAccommodationType = viewModel::selectAccommodationType,
        onSelectDateRange = viewModel::selectDateRange,
        onSelectStartWorkTime = viewModel::selectStartWorkTime,
        onSelectEndWorkTime = viewModel::selectEndWorkTime,
        onSelectWorkFocusLevel = viewModel::selectWorkFocusLevel,
        onSubmit = viewModel::createCourse,
    )
}

@Composable
private fun CreateCourseScreen(
    state: CreateCourseState,
    onRetryPetInfo: () -> Unit,
    onSelectRegion: (Region) -> Unit,
    onSelectAccommodationType: (String) -> Unit,
    onSelectDateRange: (LocalDate) -> Unit,
    onSelectStartWorkTime: (LocalTime) -> Unit,
    onSelectEndWorkTime: (LocalTime) -> Unit,
    onSelectWorkFocusLevel: (WorkFocusLevel) -> Unit,
    onSubmit: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val uiState = rememberCreateCourseUiState()

    LaunchedEffect(uiState.isWorkcationStyleExpanded, scrollState.maxValue) {
        if (uiState.isWorkcationStyleExpanded) {
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
                        append("${state.petInfo.successData?.name}")
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

        when (val petInfo = state.petInfo) {
            is UiState.Loading -> {
                MeongLoadingIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(114.dp)
                )
            }

            is UiState.Failure -> {
                MeongLoadErrorView(
                    action = LoadErrorViewAction.Retry(onRetryClick = onRetryPetInfo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(114.dp)
                )
            }

            is UiState.Empty -> {
                Text(
                    text = "등록된 반려견 정보가 없어요",
                    style = MeongTheme.typography.body.body14M,
                    color = MeongTheme.colors.gray500,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
            }

            is UiState.Success -> {
                val pet = petInfo.data

                PetProfileCard(
                    petName = pet.name,
                    imageUrl = pet.imageUrl,
                    tags = persistentListOf(
                        pet.sizeCategory.label,
                        "활동량 ${pet.activityLevel.label}",
                        "사회성 ${pet.sociability.label}",
                        pet.healthStatus.label
                    ),
                    isBordered = true
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        CreateCourseRangeInput(
            type = CreateCourseRangeInputType.WORKCATION_PERIOD,
            startText = state.selectedStartDate?.formatCourseDate(),
            endText = state.selectedEndDate?.formatCourseDate(),
            onClick = uiState::openDatePicker
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseChipSelector(
            title = "지역 선택",
            chips = Region.entries.map { it.label }.toImmutableList(),
            selectedChips = state.selectedRegion?.label
                ?.let { persistentListOf(it) }
                ?: persistentListOf(),
            onChipClick = { label ->
                Region.entries.firstOrNull { it.label == label }?.let(onSelectRegion)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseRangeInput(
            type = CreateCourseRangeInputType.WORK_TIME,
            startText = state.selectedStartWorkTime?.formatCourseTime(),
            endText = state.selectedEndWorkTime?.formatCourseTime(),
            onStartClick = { uiState.openTimePicker(WorkTimeInput.Start) },
            onEndClick = { uiState.openTimePicker(WorkTimeInput.End) },
        )

        Spacer(modifier = Modifier.height(24.dp))

        CreateCourseChipSelector(
            title = "숙소 유형",
            chips = persistentListOf("펜션", "민박", "캠핑장", "글램핑장", "호텔", "카라반"),
            selectedChips = state.selectedAccommodationType
                ?.let { persistentListOf(it) }
                ?: persistentListOf(),
            onChipClick = onSelectAccommodationType
        )

        Spacer(modifier = Modifier.height(20.dp))

        CreateCourseDropdownSelector(
            title = "워케이션 스타일",
            placeholder = "선호하는 워케이션 스타일을 선택해주세요",
            options = WorkFocusLevel.entries.map { it.label }.toImmutableList(),
            selectedOption = state.selectedWorkFocusLevel?.label,
            isExpanded = uiState.isWorkcationStyleExpanded,
            onExpandedChange = uiState::changeWorkcationStyleExpanded,
            onOptionClick = { label ->
                WorkFocusLevel.entries.firstOrNull { it.label == label }?.let(onSelectWorkFocusLevel)
                uiState.changeWorkcationStyleExpanded(false)
            },
        )

        Spacer(modifier = Modifier.height(32.dp))

        MeongButton(
            text = "코스 생성하기",
            isEnabled = state.isSubmittable && !state.isSubmitting && state.petInfo is UiState.Success,
            onClick = onSubmit
        )

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (uiState.showDatePicker) {
        CreateCourseDatePicker(
            visibleMonth = uiState.visibleMonth,
            selectedStartDate = state.selectedStartDate,
            selectedEndDate = state.selectedEndDate,
            onDismiss = uiState::closeDatePicker,
            onPreviousMonthClick = uiState::showPreviousMonth,
            onNextMonthClick = uiState::showNextMonth,
            onDateClick = onSelectDateRange,
            onSaveClick = uiState::closeDatePicker,
        )
    }

    if (uiState.showTimePicker) {
        CreateCourseTimePicker(
            initialTime = when (uiState.activeWorkTimeInput) {
                WorkTimeInput.Start -> state.selectedStartWorkTime
                WorkTimeInput.End -> state.selectedEndWorkTime
            },
            onDismiss = uiState::closeTimePicker,
            onSaveClick = { time ->
                when (uiState.activeWorkTimeInput) {
                    WorkTimeInput.Start -> onSelectStartWorkTime(time)
                    WorkTimeInput.End -> onSelectEndWorkTime(time)
                }
                uiState.closeTimePicker()
            },
        )
    }
}

@Preview
@Composable
private fun CreateCourseScreenPreview() {
    MeongTheme {
        CreateCourseScreen(
            state = CreateCourseState(),
            onRetryPetInfo = {},
            onSelectRegion = {},
            onSelectAccommodationType = {},
            onSelectDateRange = {},
            onSelectStartWorkTime = {},
            onSelectEndWorkTime = {},
            onSelectWorkFocusLevel = {},
            onSubmit = {},
        )
    }
}
