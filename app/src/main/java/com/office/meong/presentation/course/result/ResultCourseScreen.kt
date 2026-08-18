package com.office.meong.presentation.course.result

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.dragdrop.rememberDragDropState
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.disableNestedScroll
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.formatDayDate
import com.office.meong.core.common.extension.statusBarColor
import com.office.meong.core.common.util.formatDistanceKm
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongCancelAction
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.component.topbar.TopbarAction
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
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
import com.office.meong.presentation.course.result.model.CurrentDialogType
import com.office.meong.presentation.course.result.model.PlaceEditChipType
import com.office.meong.presentation.course.result.model.ResultCourseUiModel
import com.office.meong.presentation.course.result.model.RouteIndicatorType
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.ScheduleUiModel.Companion.DUMMY_SEARCHABLE_PLACES
import com.office.meong.presentation.course.result.state.ResultCourseUiState
import com.office.meong.presentation.course.result.state.rememberResultCourseUiState
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import com.office.meong.presentation.sharedcomponent.skeleton.MeongPlaceCardSkeleton
import com.office.meong.presentation.sharedcomponent.skeleton.meongShimmerTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCourseRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    viewModel: ResultCourseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is ResultCourseSideEffect.ShowToast -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    when (val course = state.course) {
        is UiState.Loading -> {
            MeongLoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Failure -> {
            MeongLoadErrorView(
                action = LoadErrorViewAction.Retry(onRetryClick = viewModel::retryCourseDetail),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        }

        is UiState.Empty -> Unit

        is UiState.Success -> {
            ResultCourseContent(
                paddingValues = paddingValues,
                course = course.data,
                selectedDayNumber = state.selectedDayNumber,
                accommodationAlternatives = state.accommodationAlternatives,
                onPreviousDayClick = viewModel::selectPreviousDay,
                onNextDayClick = viewModel::selectNextDay,
                onReorderComplete = viewModel::reorderCourseItems,
                onEditAccommodationClick = viewModel::fetchAccommodationAlternatives,
                onSelectAccommodationAlternative = viewModel::selectAccommodationAlternative,
                navigateUp = navigateUp
            )
        }
    }
}

@Composable
private fun ResultCourseContent(
    paddingValues: PaddingValues,
    course: ResultCourseUiModel,
    selectedDayNumber: Int,
    accommodationAlternatives: UiState<ImmutableList<ScheduleUiModel>>,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onReorderComplete: (dayNumber: Int, itemIds: List<Long>) -> Unit,
    onEditAccommodationClick: () -> Unit,
    onSelectAccommodationAlternative: (ScheduleUiModel) -> Unit,
    navigateUp: () -> Unit
) {
    val uiState = rememberResultCourseUiState()

    val scheduleUiModels = remember(course, selectedDayNumber) {
        mutableStateListOf(*course.dayItems[selectedDayNumber].orEmpty().toTypedArray())
    }

    val editActions = remember(uiState, scheduleUiModels, selectedDayNumber) {
        object : ResultCourseEditActions {
            override val title = object : TitleEditActions {
                override fun onClickEdit() {
                    uiState.showEditTitle()
                }

                override fun onClickComplete() {
                    uiState.hideEditTitle()
                }
            }
            override val accommodation = object : AccommodationEditActions {
                override fun onClickEdit() {
                    uiState.showEditAccommodation()
                    onEditAccommodationClick()
                }

                override fun onClickComplete() {
                    uiState.hideEditAccommodation()
                }
            }
            override val schedule = object : ScheduleEditActions {
                override fun onClickEdit() {
                    uiState.showEditSchedule()
                }

                override fun onClickComplete() {
                    uiState.hideEditSchedule()
                    onReorderComplete(selectedDayNumber, scheduleUiModels.map { it.id.toLong() })
                }
            }
        }
    }

    BackHandler {
        uiState.showExitDialog(CurrentDialogType.BACK_PRESS_EXIT)
    }

    ResultCourseScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        editActions = editActions,
        scheduleUiModels = scheduleUiModels,
        title = course.name,
        dayNumber = selectedDayNumber,
        dayDate = formatDayDate(course.startDate, selectedDayNumber),
        accommodation = course.accommodation,
        onPreviousDayClick = onPreviousDayClick,
        onNextDayClick = onNextDayClick,
        onSaveClick = {}
    )

    if (uiState.currentDialogType != null) {
        MeongDialog(
            title = uiState.currentDialogType?.title,
            subDescription = "저장하지 않은 코스는 사라져요",
            confirmAction = MeongConfirmAction(
                text = "확인",
                onClick = {
                    uiState.hideExitDialog()
                    navigateUp()
                }
            ),
            cancelAction = MeongCancelAction(
                text = "취소",
                onClick = uiState::hideExitDialog
            ),
            onDismiss = uiState::hideExitDialog
        )
    }

    if (uiState.isEditTitleVisible) {
        EditTitleBottomSheet(
            onDismiss = editActions.title::onClickComplete
        )
    }

    if (uiState.isEditAccommodationVisible) {
        EditPlaceBottomSheet(
            selectedChipType = uiState.editPlaceChipType,
            onChipClick = uiState::selectPlaceEditChip,
            onDismiss = editActions.accommodation::onClickComplete,
            alternatives = accommodationAlternatives,
            onPlaceSelected = { place ->
                onSelectAccommodationAlternative(place)
                editActions.accommodation.onClickComplete()
            }
        )
    }
}

@Composable
private fun ResultCourseScreen(
    paddingValues: PaddingValues,
    uiState: ResultCourseUiState,
    editActions: ResultCourseEditActions,
    scheduleUiModels: SnapshotStateList<ScheduleUiModel>,
    title: String,
    dayNumber: Int,
    dayDate: String,
    accommodation: ScheduleUiModel?,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val scheduleShimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = meongShimmerTheme()
    )

    val dragDropState = rememberDragDropState(
        lazyListState = lazyListState,
        items = scheduleUiModels,
        key = { it.id },
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
            onBackClick = { uiState.showExitDialog(CurrentDialogType.BACK_PRESS_EXIT) },
            onActionClick = {
                if (it == TopbarAction.CLOSE) {
                    uiState.showExitDialog(CurrentDialogType.COURSE_DELETE)
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
                    title = title,
                    onEditTitleClick = editActions.title::onClickEdit,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                if (accommodation != null) {
                    ResultCourseAccommodationSection(
                        placeName = accommodation.placeName,
                        location = accommodation.location,
                        grade = accommodation.grade.ifBlank { null },
                        thumbnailUrl = accommodation.thumbnailUrl,
                        lodgingType = accommodation.lodgingType,
                        onEditAccommodationClick = editActions.accommodation::onClickEdit,
                        onFavoriteClick = {},
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

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
                        text = if (uiState.isEditSchedule) "편집 완료" else "일정 편집",
                        style = MeongTheme.typography.label.label14Sb,
                        color = MeongTheme.colors.gray900,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .noRippleClickable(
                                onClick = if (uiState.isEditSchedule) editActions.schedule::onClickComplete else editActions.schedule::onClickEdit
                            )
                    )
                }
            }

            item {
                ResultCourseScheduleSection(
                    dayNumber = dayNumber.toString(),
                    tripDay = dayDate,
                    accommodation = accommodation,
                    onPreviousClick = onPreviousDayClick,
                    onNextClick = onNextDayClick,
                    onRouteClick = {},
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                )
            }

            itemsIndexed(
                items = scheduleUiModels,
                key = { _, item -> item.id }
            ) { index, item ->
                if (uiState.isEditSchedule) {
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
                    val nextItem = scheduleUiModels.getOrNull(index + 1)

                    ResultCourseScheduleItem(
                        count = index + 1,
                        placeName = item.placeName,
                        grade = item.grade,
                        location = item.location,
                        placeType = item.placeType,
                        isLastItem = index == scheduleUiModels.lastIndex,
                        isLoading = false,
                        shimmer = scheduleShimmer,
                        thumbnailUrl = item.thumbnailUrl,
                        lodgingType = item.lodgingType,
                        routeLength = formatDistanceKm(nextItem?.distanceFromPrevKm ?: 0.0),
                        onFavoriteClick = {},
                        onRouteClick = {},
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .animateItem()
                    )
                }
            }

            item {
                if (accommodation != null) {
                    Spacer(modifier = Modifier.height(10.dp))

                    // TODO: 숙소 API 추가되면 마지막 장소 -> 숙소 실제 거리/좌표로 교체하고 카카오맵 경로 보기 연결
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
                        placeType = accommodation.placeType.label,
                        placeName = accommodation.placeName,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                    )
                }

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
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTitleBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    MeongBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
            .imePadding()
    ) {
        MeongTopbar(
            title = "코스 이름 수정",
            isBackVisible = false,
            actionType = TopbarAction.CLOSE,
            onActionClick = { onDismiss() },
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
            trailingIcon = if (rememberTextFieldState().text.toString().isEmpty()) null else R.drawable.ic_close_filled,
            onTrailingIconClick = {
                // Todo: text 제거
            }
        )

        Spacer(modifier = Modifier.height(78.dp))

        MeongButton(
            text = "저장하기",
            isEnabled = true,
            onClick = {
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

private const val PLACE_SKELETON_ITEM_COUNT = 3

/**
 * 숙소 변경과 장소 추가를 위한 BottomSheet
 *
 * 검색 칩 선택 시 검색창이, 관심 장소 칩 선택 시 바텀시트가 화면의 70%까지 늘어나며 리스트가 노출된다.
 * `alternatives`가 주어지면(숙소 변경 플로우) 코스 아이템의 실제 대안 장소를 검색어로 필터링해 보여준다.
 * `alternatives`가 없으면 아직 서버에 전체 장소 검색 API가 없어 더미 데이터를 사용한다.
 * TODO: 장소 추가용 전체 장소 검색 API 확정 후 더미 데이터 교체
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPlaceBottomSheet(
    selectedChipType: PlaceEditChipType,
    onChipClick: (PlaceEditChipType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    alternatives: UiState<ImmutableList<ScheduleUiModel>>? = null,
    onPlaceSelected: (ScheduleUiModel) -> Unit = {}
) {
    val placeShimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = meongShimmerTheme()
    )
    var selectedPlace by remember(selectedChipType) { mutableStateOf<ScheduleUiModel?>(null) }

    MeongBottomSheet(
        onDismiss = onDismiss,
        modifier = modifier
            .imePadding()
            .disableNestedScroll()
    ) {
        MeongTopbar(
            title = "장소 추가",
            isBackVisible = false,
            actionType = TopbarAction.CLOSE,
            onActionClick = { onDismiss() },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlaceEditChipType.entries.forEach { chipType ->
                MeongChip(
                    chipText = chipType.label,
                    chipType = ChipType.LARGE,
                    isSelected = chipType == selectedChipType,
                    modifier = Modifier
                        .noRippleClickable {
                            onChipClick(chipType)
                        }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
        ) {
            when (selectedChipType) {
                PlaceEditChipType.SEARCH -> {
                    val searchFieldState = rememberTextFieldState()
                    val query = searchFieldState.text.toString()

                    MeongTextField(
                        state = searchFieldState,
                        placeholder = "장소를 검색해주세요",
                        leadingIcon = R.drawable.ic_search,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (alternatives != null) {
                        val allPlaces = (alternatives as? UiState.Success)?.data ?: persistentListOf()
                        val filteredPlaces = remember(query, allPlaces) {
                            if (query.isBlank()) {
                                allPlaces
                            } else {
                                allPlaces.filter { it.placeName.contains(query, ignoreCase = true) }.toPersistentList()
                            }
                        }

                        PlaceEditResultList(
                            isLoading = alternatives is UiState.Loading,
                            places = filteredPlaces,
                            shimmer = placeShimmer,
                            emptyTitle = "변경 가능한 숙소가 없어요",
                            emptyDescription = "다른 검색어를 입력해보세요",
                            onFavoriteClick = {},
                            selectedPlaceId = selectedPlace?.id,
                            onPlaceClick = { selectedPlace = it }
                        )
                    } else {
                        var isSearchLoading by remember { mutableStateOf(false) }
                        var searchResults by remember { mutableStateOf(persistentListOf<ScheduleUiModel>()) }

                        LaunchedEffect(query) {
                            if (query.isBlank()) {
                                isSearchLoading = false
                                searchResults = persistentListOf()
                                return@LaunchedEffect
                            }

                            isSearchLoading = true
                            delay(3.seconds)
                            searchResults = DUMMY_SEARCHABLE_PLACES.filter { it.placeName.contains(query) }.toPersistentList()
                            isSearchLoading = false
                        }

                        if (query.isNotBlank()) {
                            PlaceEditResultList(
                                isLoading = isSearchLoading,
                                places = searchResults,
                                shimmer = placeShimmer,
                                emptyTitle = "검색 결과가 없어요",
                                emptyDescription = "다른 검색어를 입력해주세요",
                                onFavoriteClick = {},
                                onPlaceClick = onPlaceSelected
                            )
                        }
                    }
                }

                PlaceEditChipType.FAVORITE -> {
                    val favoritePlaces = remember {
                        persistentListOf(
                            ScheduleUiModel(id = "favorite-1", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A"),
                            ScheduleUiModel(id = "favorite-2", placeType = PlaceType.RESTAURANT, placeName = "댕댕이 맛집", grade = "A"),
                            ScheduleUiModel(id = "favorite-3", placeType = PlaceType.SIGHTSEEING, placeName = "산책하기 좋은 공원", grade = "A"),
                        )
                    }
                    var isFavoriteLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(3.seconds)
                        isFavoriteLoading = false
                    }

                    PlaceEditResultList(
                        isLoading = isFavoriteLoading,
                        places = favoritePlaces,
                        shimmer = placeShimmer,
                        emptyTitle = "저장된 관심 장소가 없어요",
                        emptyDescription = "마음에 드는 워케이션 장소를 탐색해 보세요! ",
                        onFavoriteClick = {},
                        selectedPlaceId = selectedPlace?.id,
                        onPlaceClick = { selectedPlace = it }
                    )
                }
            }
        }

        if (alternatives != null) {
            Spacer(modifier = Modifier.height(20.dp))

            MeongButton(
                text = "이 장소로 변경",
                isEnabled = selectedPlace != null,
                onClick = { selectedPlace?.let(onPlaceSelected) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * 검색/관심 장소 결과를 로딩(shimmer) - 빈 화면 - 목록 중 하나로 그려주는 공용 리스트
 * */
@Composable
private fun PlaceEditResultList(
    isLoading: Boolean,
    places: ImmutableList<ScheduleUiModel>,
    shimmer: Shimmer,
    emptyTitle: String,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    emptyDescription: String? = null,
    selectedPlaceId: String? = null,
    onPlaceClick: (ScheduleUiModel) -> Unit = {},
) {
    val maxListHeight = with(LocalConfiguration.current) { (screenHeightDp * 0.7f).dp }

    when {
        isLoading -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(maxListHeight),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = PLACE_SKELETON_ITEM_COUNT) {
                    MeongPlaceCardSkeleton(
                        shimmer = shimmer
                    )
                }
            }
        }

        places.isEmpty() -> {
            PlaceEditEmptyView(
                title = emptyTitle,
                description = emptyDescription,
                modifier = modifier
                    .fillMaxWidth()
                    .height(maxListHeight)
                    .padding(horizontal = 54.dp)
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .height(maxListHeight),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = places,
                    key = { it.id }
                ) { place ->
                    MeongPlaceCard(
                        placeName = place.placeName,
                        location = place.location,
                        grade = place.grade,
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteClick(place.id) },
                        placeType = place.placeType,
                        isBordered = true,
                        isSelected = place.id == selectedPlaceId,
                        modifier = Modifier.noRippleClickable(onClick = { onPlaceClick(place) })
                    )
                }
            }
        }
    }
}

@Composable
private fun PlaceEditEmptyView(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray800,
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray500,
            )
        }
    }
}

@Preview
@Composable
private fun ResultCourseScreenPreview() {
    MeongTheme {
        ResultCourseScreen(
            paddingValues = PaddingValues(),
            uiState = rememberResultCourseUiState(),
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
            scheduleUiModels = remember {
                mutableStateListOf(
                    ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "3", placeType = PlaceType.OTHER, placeName = "프렌즈애견펜션", grade = "A"),
                )
            },
            title = "강릉 2박 3일 워케이션",
            dayNumber = 2,
            dayDate = "8.11",
            accommodation = ScheduleUiModel(id = "4", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A", location = "서울시 강남구"),
            onPreviousDayClick = {},
            onNextDayClick = {},
            onSaveClick = {}
        )
    }
}
