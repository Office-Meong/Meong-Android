package com.office.meong.presentation.course.result

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.statusBarColor
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongCancelAction
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.component.topbar.TopbarAction
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.result.action.AccommodationEditActions
import com.office.meong.presentation.course.result.action.ResultCourseEditActions
import com.office.meong.presentation.course.result.action.ScheduleEditActions
import com.office.meong.presentation.course.result.action.TitleEditActions
import com.office.meong.presentation.course.result.component.ResultCourseAccommodationSection
import com.office.meong.presentation.course.result.component.ResultCourseEditScheduleItem
import com.office.meong.presentation.course.result.component.ResultCourseInfoHolder
import com.office.meong.presentation.course.result.component.ResultCoursePlaceSummaryItem
import com.office.meong.presentation.course.result.component.ResultCourseScheduleItem
import com.office.meong.presentation.course.result.component.ResultCourseScheduleSection
import com.office.meong.presentation.course.result.component.ResultCourseTopSection
import com.office.meong.presentation.course.result.component.RouteIndicator
import com.office.meong.presentation.course.result.state.rememberScheduleDragDropState
import com.office.meong.presentation.course.result.model.CurrentDialogType
import com.office.meong.presentation.course.result.model.RouteIndicatorType
import com.office.meong.presentation.course.result.model.ScheduleUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCourseRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit
) {
    var currentDialogType by remember { mutableStateOf<CurrentDialogType?>(null) }
    var isEditTitle by remember { mutableStateOf(false) }
    var isEditSchedule by remember { mutableStateOf(false) }
    var isEditAccommodation by remember { mutableStateOf(false) }

    val scheduleUiModels = remember { mutableStateListOf(
        ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "프렌즈애견펜션", grade = "A"),
        ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "프렌즈애견펜션", grade = "A"),
        ScheduleUiModel(id = "3", placeType = PlaceType.OTHER, placeName = "프렌즈애견펜션", grade = "A"),
        ScheduleUiModel(id = "4", placeType = PlaceType.SIGHTSEEING, placeName = "프렌즈애견펜션", grade = "A"),
    ) }

    val editActions = remember {
        object : ResultCourseEditActions {
            override val title = object : TitleEditActions {
                override fun onClickEdit() {
                    isEditTitle = true
                }

                override fun onClickComplete() {
                    isEditTitle = false
                }
            }
            override val accommodation = object : AccommodationEditActions {
                override fun onClickEdit() {
                    isEditAccommodation = true
                }

                override fun onClickComplete() {
                    isEditAccommodation = false
                }
            }
            override val schedule = object : ScheduleEditActions {
                override fun onClickEdit() {
                    isEditSchedule = true
                }

                override fun onClickComplete() {
                    isEditSchedule = false
                }
            }
        }
    }

    BackHandler {
        currentDialogType = CurrentDialogType.BACK_PRESS_EXIT
    }

    ResultCourseScreen(
        paddingValues = paddingValues,
        scheduleUiModels = scheduleUiModels,
        isEditSchedule = isEditSchedule,
        editActions = editActions,
        onBackClick = {
            currentDialogType = CurrentDialogType.BACK_PRESS_EXIT
        },
        removeCurrentRoute = {
            currentDialogType = CurrentDialogType.COURSE_DELETE
        }
    )

    if (currentDialogType != null) {
        MeongDialog(
            title = currentDialogType?.title,
            subDescription = "저장하지 않은 코스는 사라져요",
            confirmAction = MeongConfirmAction(
                text = "확인",
                onClick = {
                    currentDialogType = null
                    navigateUp()
                }
            ),
            cancelAction = MeongCancelAction(
                text = "취소",
                onClick = {
                    currentDialogType = null
                }
            ),
            onDismiss = {
                currentDialogType = null
            }
        )
    }

    if (isEditTitle) {
        EditTitleBottomSheet(
            onEditTitle = {
                isEditTitle = it
            }
        )
    }
}

@Composable
private fun ResultCourseScreen(
    paddingValues: PaddingValues,
    scheduleUiModels: SnapshotStateList<ScheduleUiModel>,
    isEditSchedule: Boolean,
    editActions: ResultCourseEditActions,
    onBackClick: () -> Unit,
    removeCurrentRoute: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    val dragDropState = rememberScheduleDragDropState(
        lazyListState = lazyListState,
        items = scheduleUiModels,
        onMove = { fromId, toId ->
            val fromIndex = scheduleUiModels.indexOfFirst { it.id == fromId }
            val toIndex = scheduleUiModels.indexOfFirst { it.id == toId }
            if (fromIndex != -1 && toIndex != -1) {
                scheduleUiModels.add(toIndex, scheduleUiModels.removeAt(fromIndex))
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    )

    LaunchedEffect(dragDropState) {
        dragDropState.consumeScrollRequests { diff ->
            lazyListState.scrollBy(diff)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MeongTheme.colors.gray50
            )
            .statusBarColor(
                backgroundColor = MeongTheme.colors.white
            )
            .padding(paddingValues)
    ) {
        MeongTopbar(
            isBackVisible = true,
            actionType = TopbarAction.CLOSE,
            onBackClick = onBackClick,
            onActionClick = {
                if (it == TopbarAction.CLOSE) {
                    removeCurrentRoute()
                }
            },
        )

        ResultCourseTopSection(
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                ResultCourseInfoHolder(
                    onEditTitleClick = editActions.title::onClickEdit,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                ResultCourseAccommodationSection(
                    onEditAccommodationClick = editActions.accommodation::onClickEdit,
                    onFavoriteClick = {},
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "일정",
                        style = MeongTheme.typography.label.label14Sb,
                        color = MeongTheme.colors.gray900,
                    )

                    Text(
                        text = if (isEditSchedule) "편집 완료" else "일정 편집",
                        style = MeongTheme.typography.label.label14Sb,
                        color = MeongTheme.colors.gray900,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .noRippleClickable(
                                onClick = if (isEditSchedule) editActions.schedule::onClickComplete else editActions.schedule::onClickEdit
                            )
                    )
                }
            }

            item {
                ResultCourseScheduleSection(
                    dayNumber = "2",
                    tripDay = "8.11",
                    onPreviousClick = {},
                    onNextClick = {},
                    onRouteClick = {},
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            itemsIndexed(
                items = scheduleUiModels,
                key = { _, item -> item.id }
            ) { index, item ->
                if (isEditSchedule) {
                    val isDragging = dragDropState.draggingItemKey == item.id

                    ResultCourseEditScheduleItem(
                        placeType = item.placeType,
                        placeName = item.placeName,
                        onDragStart = {
                            dragDropState.onDragStart(item.id)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        onDrag = { dragDropState.onDrag(it) },
                        onDragEnd = { dragDropState.onDragEnd() },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .let { if (isDragging) it else it.animateItem() }
                            .zIndex(if (isDragging) 1f else 0f)
                            .graphicsLayer {
                                translationY =
                                    if (isDragging) dragDropState.draggingItemOffset else 0f
                            }
                    )
                } else {
                    ResultCourseScheduleItem(
                        count = index + 1,
                        placeName = item.placeName,
                        grade = item.grade,
                        location = item.location,
                        placeType = item.placeType,
                        isLastItem = index == scheduleUiModels.lastIndex,
                        onFavoriteClick = {},
                        onRouteClick = {},
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .animateItem()
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                RouteIndicator(
                    routeLength = "1.2",
                    onRouteClick = {},
                    routeIndicatorType = RouteIndicatorType.END,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                ResultCoursePlaceSummaryItem(
                    placeType = "숙소",
                    placeName = "프렌즈애견펜션",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MeongTheme.colors.white
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            MeongButton(
                text = "코스 저장하기",
                isEnabled = true,
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTitleBottomSheet(
    onEditTitle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    MeongBottomSheet(
        onDismiss = {
            onEditTitle(false)
        },
        modifier = modifier
            .imePadding()
    ) {
        MeongTopbar(
            title = "코스 이름 수정",
            isBackVisible = false,
            actionType = TopbarAction.CLOSE,
            onActionClick = { onEditTitle(false) },
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "코스 이름",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray700,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        MeongTextField(
            state = rememberTextFieldState(),
            placeholder = "코스 이름을 입력해주세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trailingIcon = if (rememberTextFieldState().text.toString().isEmpty()) R.drawable.ic_close_filled else null,
            onTrailingIconClick = {
                // Todo: text 제거
            }
        )

        Spacer(modifier = Modifier.height(78.dp))

        MeongButton(
            text = "저장하기",
            isEnabled = true,
            onClick = {
                onEditTitle(false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}



@Preview
@Composable
private fun ResultCourseScreenPreview() {
    MeongTheme {
        ResultCourseScreen(
            paddingValues = PaddingValues(),
            scheduleUiModels = remember {
                mutableStateListOf(
                    ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "3", placeType = PlaceType.OTHER, placeName = "프렌즈애견펜션", grade = "A"),
                )
            },
            onBackClick = {},
            isEditSchedule = false,
            editActions = remember {
                object : ResultCourseEditActions {
                    override val title = object : TitleEditActions {
                        override fun onClickEdit() {}
                        override fun onClickComplete() {}
                    }
                    override val accommodation = object : AccommodationEditActions {
                        override fun onClickEdit() {}
                        override fun onClickComplete() {}
                    }
                    override val schedule = object : ScheduleEditActions {
                        override fun onClickEdit() {}
                        override fun onClickComplete() {}
                    }
                }
            },
            removeCurrentRoute = {}
        )
    }
}
