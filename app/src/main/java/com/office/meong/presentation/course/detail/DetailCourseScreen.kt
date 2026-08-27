package com.office.meong.presentation.course.detail

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.dragdrop.rememberDragDropState
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.disableNestedScroll
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.openKakaoMapRoute
import com.office.meong.core.common.extension.statusBarColor
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.formatDayDate
import com.office.meong.core.common.util.formatDistanceKm
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.component.indicator.MeongLoadingIndicator
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.component.topbar.TopbarAction
import com.office.meong.core.designsystem.component.view.LoadErrorViewAction
import com.office.meong.core.designsystem.component.view.MeongLoadErrorView
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.core.model.place.PlaceType
import com.office.meong.core.model.region.Region
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.course.detail.action.AccommodationEditActions
import com.office.meong.presentation.course.detail.action.DetailCourseEditActions
import com.office.meong.presentation.course.detail.action.ScheduleEditActions
import com.office.meong.presentation.course.detail.action.TitleEditActions
import com.office.meong.presentation.course.detail.component.DetailCourseAccommodationSection
import com.office.meong.presentation.course.detail.component.DetailCourseDeleteDialog
import com.office.meong.presentation.course.detail.component.DetailCourseEditScheduleItem
import com.office.meong.presentation.course.detail.component.DetailCourseInfoHolder
import com.office.meong.presentation.course.detail.component.DetailCoursePlaceSummaryItem
import com.office.meong.presentation.course.detail.component.DetailCourseRouteIndicator
import com.office.meong.presentation.course.detail.component.DetailCourseScheduleItem
import com.office.meong.presentation.course.detail.component.DetailCourseScheduleSection
import com.office.meong.presentation.course.detail.component.DetailCourseTopAction
import com.office.meong.presentation.course.detail.model.DetailCourseRouteIndicatorType
import com.office.meong.presentation.course.detail.model.DetailCourseUiModel
import com.office.meong.presentation.course.detail.model.PlaceEditChipType
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.detail.state.DetailCourseUiState
import com.office.meong.presentation.course.detail.state.rememberDetailCourseUiState
import com.office.meong.presentation.sharedcomponent.MeongPlaceCard
import com.office.meong.presentation.sharedcomponent.PetProfileCard
import com.office.meong.presentation.sharedcomponent.skeleton.MeongPlaceCardSkeleton
import com.office.meong.presentation.sharedcomponent.skeleton.meongShimmerTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCourseRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit = {},
    viewModel: DetailCourseViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is DetailCourseSideEffect.ShowToast -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
            is DetailCourseSideEffect.NavigateUp -> navigateUp()
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
            DetailCourseContent(
                paddingValues = paddingValues,
                course = course.data,
                petInfo = state.petInfo,
                selectedDayNumber = state.selectedDayNumber,
                accommodationAlternatives = state.accommodationAlternatives,
                favoritePlaces = state.favoritePlaces,
                favoritePlaceIds = state.favoritePlaceIds,
                placeSearchResults = state.placeSearchResults,
                scheduleItemAlternatives = state.scheduleItemAlternatives,
                onPreviousDayClick = viewModel::selectPreviousDay,
                onNextDayClick = viewModel::selectNextDay,
                onBackClick = navigateUp,
                onReorderComplete = viewModel::reorderCourseItems,
                onAddCourseItem = viewModel::addCourseItem,
                onRetryPetInfo = viewModel::retryPetInfo,
                onDeleteClick = viewModel::removeCourse,
                onUpdateCourseName = viewModel::updateCourseName,
                onEditAccommodationClick = viewModel::fetchAccommodationAlternatives,
                onSelectAccommodationAlternative = viewModel::selectAccommodationAlternative,
                onPlaceSearchQueryChanged = viewModel::onPlaceSearchQueryChanged,
                onFavoriteToggle = viewModel::onFavoriteToggle,
                onEditScheduleItemClick = viewModel::fetchScheduleItemAlternatives,
                onSelectScheduleItemAlternative = viewModel::selectScheduleItemAlternative,
                onDeleteScheduleItemClick = viewModel::deleteScheduleItem
            )
        }
    }
}

@Composable
private fun DetailCourseContent(
    paddingValues: PaddingValues,
    course: DetailCourseUiModel,
    petInfo: UiState<PetInfo>,
    selectedDayNumber: Int,
    accommodationAlternatives: UiState<ImmutableList<ScheduleUiModel>>,
    favoritePlaces: UiState<ImmutableList<ScheduleUiModel>>,
    favoritePlaceIds: ImmutableSet<Long>,
    placeSearchResults: UiState<ImmutableList<ScheduleUiModel>>,
    scheduleItemAlternatives: UiState<ImmutableList<ScheduleUiModel>>,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onBackClick: () -> Unit,
    onReorderComplete: (dayNumber: Int, itemIds: List<Long>) -> Unit,
    onAddCourseItem: (dayNumber: Int, placeId: Long) -> Unit,
    onDeleteClick: () -> Unit,
    onUpdateCourseName: (String) -> Unit,
    onRetryPetInfo: () -> Unit,
    onEditAccommodationClick: () -> Unit,
    onSelectAccommodationAlternative: (ScheduleUiModel) -> Unit,
    onPlaceSearchQueryChanged: (String) -> Unit,
    onFavoriteToggle: (ScheduleUiModel) -> Unit,
    onEditScheduleItemClick: (itemId: Long) -> Unit,
    onSelectScheduleItemAlternative: (ScheduleUiModel) -> Unit,
    onDeleteScheduleItemClick: (itemId: Long) -> Unit
) {
    val uiState = rememberDetailCourseUiState()

    val scheduleUiModels = remember(course, selectedDayNumber) {
        mutableStateListOf(
            *course.dayItems[selectedDayNumber].orEmpty()
                .filterNot { it.placeType == PlaceType.ACCOMMODATION }
                .toTypedArray()
        )
    }

    val editActions = remember(uiState, scheduleUiModels, selectedDayNumber) {
        object : DetailCourseEditActions {
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
                    val accommodationId = course.dayItems[selectedDayNumber].orEmpty()
                        .firstOrNull { it.placeType == PlaceType.ACCOMMODATION }?.id?.toLong()
                    val itemIds = listOfNotNull(accommodationId) + scheduleUiModels.map { it.id.toLong() }
                    onReorderComplete(selectedDayNumber, itemIds)
                }
            }
        }
    }

    DetailCourseScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        editActions = editActions,
        scheduleUiModels = scheduleUiModels,
        course = course,
        dayNumber = selectedDayNumber,
        petInfo = petInfo,
        favoritePlaceIds = favoritePlaceIds,
        onFavoriteToggle = onFavoriteToggle,
        onRetryPetInfo = onRetryPetInfo,
        onPreviousDayClick = onPreviousDayClick,
        onNextDayClick = onNextDayClick,
        onBackClick = onBackClick,
        onChangeScheduleItemClick = { itemId ->
            uiState.showEditScheduleItem()
            onEditScheduleItemClick(itemId)
        },
        onDeleteScheduleItemClick = onDeleteScheduleItemClick
    )

    if (uiState.isEditTitleVisible) {
        DetailCourseEditTitleBottomSheet(
            initialTitle = course.name,
            onDismiss = editActions.title::onClickComplete,
            onSave = { name ->
                onUpdateCourseName(name)
                editActions.title.onClickComplete()
            }
        )
    }

    if (uiState.isEditAccommodationVisible) {
        DetailCourseEditPlaceBottomSheet(
            selectedChipType = uiState.editPlaceChipType,
            onChipClick = uiState::selectPlaceEditChip,
            onDismiss = editActions.accommodation::onClickComplete,
            alternatives = accommodationAlternatives,
            favoritePlaces = favoritePlaces,
            favoritePlaceIds = favoritePlaceIds,
            onFavoriteToggle = onFavoriteToggle,
            onPlaceSelected = { place ->
                onSelectAccommodationAlternative(place)
                editActions.accommodation.onClickComplete()
            }
        )
    }

    if (uiState.isEditScheduleItemVisible) {
        DetailCourseEditPlaceBottomSheet(
            title = "다른 장소로 변경",
            selectedChipType = uiState.editPlaceChipType,
            onChipClick = uiState::selectPlaceEditChip,
            onDismiss = uiState::hideEditScheduleItem,
            alternatives = scheduleItemAlternatives,
            favoritePlaces = favoritePlaces,
            favoritePlaceIds = favoritePlaceIds,
            onFavoriteToggle = onFavoriteToggle,
            onPlaceSelected = { place ->
                onSelectScheduleItemAlternative(place)
                uiState.hideEditScheduleItem()
            }
        )
    }

    if (uiState.isAddPlaceVisible) {
        DetailCourseEditPlaceBottomSheet(
            selectedChipType = uiState.editPlaceChipType,
            onChipClick = uiState::selectPlaceEditChip,
            onDismiss = uiState::hideAddPlace,
            favoritePlaces = favoritePlaces,
            favoritePlaceIds = favoritePlaceIds,
            onFavoriteToggle = onFavoriteToggle,
            placeSearchResults = placeSearchResults,
            onPlaceSearchQueryChanged = onPlaceSearchQueryChanged,
            onPlaceSelected = { place ->
                place.placeId?.let { placeId ->
                    onAddCourseItem(selectedDayNumber, placeId)
                    uiState.hideAddPlace()
                }
            }
        )
    }

    if (uiState.isDeleteDialogVisible) {
        DetailCourseDeleteDialog(
            onDismiss = uiState::hideDeleteDialog,
            onDelete = onDeleteClick
        )
    }
}

@Composable
private fun DetailCourseScreen(
    paddingValues: PaddingValues,
    uiState: DetailCourseUiState,
    editActions: DetailCourseEditActions,
    scheduleUiModels: SnapshotStateList<ScheduleUiModel>,
    course: DetailCourseUiModel,
    dayNumber: Int,
    petInfo: UiState<PetInfo>,
    favoritePlaceIds: ImmutableSet<Long>,
    onFavoriteToggle: (ScheduleUiModel) -> Unit,
    onRetryPetInfo: () -> Unit,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    onBackClick: () -> Unit,
    onChangeScheduleItemClick: (itemId: Long) -> Unit,
    onDeleteScheduleItemClick: (itemId: Long) -> Unit
) {
    val title = course.name
    val location = course.region.label
    val tripPeriod = course.tripPeriod
    val totalDays = course.totalDays
    val dayDate = formatDayDate(course.startDate, dayNumber)
    val accommodation = course.accommodation

    val lazyListState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val latestMoreButtonCoordinates = remember { arrayOfNulls<LayoutCoordinates>(1) }
    var moreButtonCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    BackHandler(enabled = uiState.isTopActionVisible) {
        uiState.hideTopAction()
        onBackClick()
    }

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

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MeongTheme.colors.gray50)
                .statusBarColor(backgroundColor = MeongTheme.colors.gray50)
                .padding(paddingValues)
        ) {
            MeongTopbar(
                title = "코스 상세",
                actionType = TopbarAction.MORE,
                onBackClick = onBackClick,
                isStrokeVisible = true,
                containerColor = MeongTheme.colors.gray50,
                onActionClick = {
                    if (it == TopbarAction.MORE) {
                        uiState.toggleTopAction()
                        if (uiState.isTopActionVisible) {
                            moreButtonCoordinates = latestMoreButtonCoordinates[0]
                        }
                    }
                },
                onActionPositioned = { latestMoreButtonCoordinates[0] = it }
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    Spacer(Modifier.height(20.dp))

                    DetailCourseInfoHolder(
                        title = title,
                        location = location,
                        tripDay = tripPeriod,
                        onEditTitleClick = editActions.title::onClickEdit,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    Text(
                        text = "함께할 반려견",
                        style = MeongTheme.typography.label.label14Sb,
                        color = MeongTheme.colors.gray900,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    when (petInfo) {
                        is UiState.Loading -> {
                            MeongLoadingIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(114.dp)
                            )
                        }

                        is UiState.Failure -> {
                            MeongLoadErrorView(
                                action = LoadErrorViewAction.Retry(onRetryClick = onRetryPetInfo),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(114.dp)
                            )
                        }

                        is UiState.Empty -> Unit

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
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }

                    if (accommodation != null) {
                        Spacer(Modifier.height(24.dp))

                        DetailCourseAccommodationSection(
                            placeName = accommodation.placeName,
                            location = accommodation.location,
                            grade = accommodation.grade.ifBlank { null },
                            thumbnailUrl = accommodation.thumbnailUrl,
                            lodgingType = accommodation.lodgingType,
                            isFavorite = accommodation.placeId in favoritePlaceIds,
                            onChangeAccommodationClick = editActions.accommodation::onClickEdit,
                            onFavoriteClick = { onFavoriteToggle(accommodation) },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "일정",
                            style = MeongTheme.typography.label.label14Sb,
                            color = MeongTheme.colors.gray900
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

                    DetailCourseScheduleSection(
                        dayNumber = dayNumber.toString(),
                        isFirstDay = dayNumber == 1,
                        isLastDay = dayNumber == totalDays,
                        tripDay = dayDate,
                        routeLength = formatDistanceKm(scheduleUiModels.firstOrNull()?.distanceFromPrevKm ?: 0.0),
                        accommodation = accommodation,
                        onPreviousClick = onPreviousDayClick,
                        onNextClick = onNextDayClick,
                        onRouteClick = {
                            val firstItem = scheduleUiModels.firstOrNull()
                            if (accommodation != null && firstItem != null) {
                                context.openKakaoMapRoute(
                                    originName = accommodation.location.ifBlank { accommodation.placeName },
                                    originLatitude = accommodation.latitude,
                                    originLongitude = accommodation.longitude,
                                    destinationName = firstItem.location.ifBlank { firstItem.placeName },
                                    destinationLatitude = firstItem.latitude,
                                    destinationLongitude = firstItem.longitude,
                                    type = "car"
                                )
                            }
                        },
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }

                itemsIndexed(
                    items = scheduleUiModels,
                    key = { _, item -> item.id }
                ) { index, item ->
                    if (uiState.isEditSchedule) {
                        val isDragging = dragDropState.draggingItemKey == item.id

                        DetailCourseEditScheduleItem(
                            placeType = item.placeType,
                            placeName = item.placeName,
                            onDragStart = {
                                dragDropState.onDragStart(item.id)
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            onDrag = { dragDropState.onDrag(it) },
                            onDragEnd = { dragDropState.onDragEnd() },
                            onChangePlaceClick = { onChangeScheduleItemClick(item.id.toLong()) },
                            onDeleteClick = { onDeleteScheduleItemClick(item.id.toLong()) },
                            modifier = Modifier
                                .let { if (isDragging) it else it.animateItem() }
                                .zIndex(if (isDragging) 1f else 0f)
                                .graphicsLayer {
                                    translationY =
                                        if (isDragging) dragDropState.draggingItemOffset else 0f
                                }
                        )
                    } else {
                        val nextItem = scheduleUiModels.getOrNull(index + 1)

                        DetailCourseScheduleItem(
                            count = index + 1,
                            placeName = item.placeName,
                            placeType = item.placeType,
                            location = item.location.ifBlank { "카카오맵으로 확인해요" },
                            grade = item.grade.ifBlank { null },
                            routeLength = formatDistanceKm(nextItem?.distanceFromPrevKm ?: 0.0),
                            isLastItem = index == scheduleUiModels.lastIndex,
                            thumbnailUrl = item.thumbnailUrl,
                            lodgingType = item.lodgingType,
                            isFavorite = item.placeId in favoritePlaceIds,
                            onFavoriteClick = { onFavoriteToggle(item) },
                            onRouteClick = {
                                nextItem?.let {
                                    context.openKakaoMapRoute(
                                        originName = item.location.ifBlank { item.placeName },
                                        originLatitude = item.latitude,
                                        originLongitude = item.longitude,
                                        destinationName = it.location.ifBlank { it.placeName },
                                        destinationLatitude = it.latitude,
                                        destinationLongitude = it.longitude,
                                        type = "car"
                                    )
                                }
                            },
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .animateItem()
                        )
                    }
                }

                item {
                    if (accommodation != null) {
                        Spacer(Modifier.height(10.dp))

                        DetailCourseRouteIndicator(
                            routeLength = formatDistanceKm(course.dayReturnToAccommKm[dayNumber] ?: 0.0),
                            onRouteClick = {},
                            routeIndicatorType = DetailCourseRouteIndicatorType.END,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        DetailCoursePlaceSummaryItem(
                            placeType = accommodation.placeType.label,
                            placeName = accommodation.placeName,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                    Spacer(Modifier.height(40.dp))
                }

                item {
                    if (uiState.isEditSchedule) {
                        Spacer(modifier = Modifier.height(10.dp))

                        MeongPillButton(
                            text = "장소 추가",
                            onClick = uiState::showAddPlace,
                            prefixIcon = R.drawable.ic_plus
                        )
                    }
                }
            }
        }

        val coordinates = moreButtonCoordinates
        if (uiState.isTopActionVisible && coordinates != null) {
            val positionProvider = remember(coordinates) {
                object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        val buttonBounds = coordinates.boundsInWindow()
                        val x = buttonBounds.right - popupContentSize.width
                        val y = buttonBounds.bottom
                        return IntOffset(x.toInt(), y.toInt())
                    }
                }
            }

            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { uiState.hideTopAction() },
                properties = PopupProperties(focusable = false)
            ) {
                DetailCourseTopAction(
                    onClick = {
                        uiState.hideTopAction()
                        uiState.showDeleteDialog()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailCourseEditTitleBottomSheet(
    initialTitle: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val titleFieldState = rememberTextFieldState(initialText = initialTitle)

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
            state = titleFieldState,
            placeholder = "코스 이름을 입력해주세요",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trailingIcon = if (titleFieldState.text.isEmpty()) null else R.drawable.ic_close_filled,
            onTrailingIconClick = {
                titleFieldState.edit { replace(0, length, "") }
            }
        )

        Spacer(modifier = Modifier.height(78.dp))

        MeongButton(
            text = "저장하기",
            isEnabled = titleFieldState.text.isNotBlank(),
            onClick = { onSave(titleFieldState.text.toString()) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * `alternatives`가 주어지면(숙소 변경 플로우) 코스 아이템의 실제 대안 장소를 검색어로 필터링해 보여준다.
 * `alternatives`가 없으면(장소 추가 플로우) `placeSearchResults`를 통해 실제 장소 검색 API 결과를 보여준다.
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailCourseEditPlaceBottomSheet(
    selectedChipType: PlaceEditChipType,
    onChipClick: (PlaceEditChipType) -> Unit,
    onDismiss: () -> Unit,
    favoritePlaces: UiState<ImmutableList<ScheduleUiModel>>,
    favoritePlaceIds: ImmutableSet<Long>,
    onFavoriteToggle: (ScheduleUiModel) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "장소 추가",
    alternatives: UiState<ImmutableList<ScheduleUiModel>>? = null,
    placeSearchResults: UiState<ImmutableList<ScheduleUiModel>> = UiState.Empty,
    onPlaceSearchQueryChanged: (String) -> Unit = {},
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
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(0.7f)
        ) {
            MeongTopbar(
                title = title,
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
                    .weight(1f)
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

                            DetailCoursePlaceEditResultList(
                                isLoading = alternatives is UiState.Loading,
                                places = filteredPlaces,
                                shimmer = placeShimmer,
                                emptyTitle = "변경 가능한 숙소가 없어요",
                                emptyDescription = "다른 검색어를 입력해보세요",
                                favoritePlaceIds = favoritePlaceIds,
                                onFavoriteClick = onFavoriteToggle,
                                selectedPlaceId = selectedPlace?.id,
                                onPlaceClick = { selectedPlace = it },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            LaunchedEffect(query) {
                                onPlaceSearchQueryChanged(query)
                            }

                            if (query.isNotBlank()) {
                                DetailCoursePlaceEditResultList(
                                    isLoading = placeSearchResults is UiState.Loading,
                                    places = placeSearchResults.successData ?: persistentListOf(),
                                    shimmer = placeShimmer,
                                    emptyTitle = "검색 결과가 없어요",
                                    emptyDescription = "다른 검색어를 입력해주세요",
                                    onFavoriteClick = {},
                                    onPlaceClick = onPlaceSelected,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    PlaceEditChipType.FAVORITE -> {
                        DetailCoursePlaceEditResultList(
                            isLoading = favoritePlaces is UiState.Loading,
                            places = favoritePlaces.successData ?: persistentListOf(),
                            shimmer = placeShimmer,
                            emptyTitle = "저장된 관심 장소가 없어요",
                            emptyDescription = "마음에 드는 워케이션 장소를 탐색해 보세요! ",
                            favoritePlaceIds = favoritePlaceIds,
                            onFavoriteClick = onFavoriteToggle,
                            selectedPlaceId = if (alternatives != null) selectedPlace?.id else null,
                            onPlaceClick = if (alternatives != null) {
                                { selectedPlace = it }
                            } else {
                                onPlaceSelected
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (alternatives != null) {
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
                        text = "이 장소로 변경",
                        isEnabled = selectedPlace != null,
                        onClick = { selectedPlace?.let(onPlaceSelected) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

/**
 * 검색/관심 장소 결과를 로딩(shimmer) - 빈 화면 - 목록 중 하나로 그려주는 공용 리스트
 * */
@Composable
private fun DetailCoursePlaceEditResultList(
    isLoading: Boolean,
    places: ImmutableList<ScheduleUiModel>,
    shimmer: Shimmer,
    emptyTitle: String,
    onFavoriteClick: (ScheduleUiModel) -> Unit,
    modifier: Modifier = Modifier,
    emptyDescription: String? = null,
    favoritePlaceIds: ImmutableSet<Long> = persistentSetOf(),
    selectedPlaceId: String? = null,
    onPlaceClick: (ScheduleUiModel) -> Unit = {},
) {
    when {
        isLoading -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .disableNestedScroll(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = 3) {
                    MeongPlaceCardSkeleton(shimmer = shimmer)
                }
            }
        }

        places.isEmpty() -> {
            DetailCoursePlaceEditEmptyView(
                title = emptyTitle,
                description = emptyDescription,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 54.dp)
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .disableNestedScroll(),
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
                        isFavorite = place.placeId in favoritePlaceIds,
                        onFavoriteClick = { onFavoriteClick(place) },
                        placeType = place.placeType,
                        isBordered = true,
                        isSelected = place.id == selectedPlaceId,
                        modifier = Modifier.noRippleClickable(onClick = { onPlaceClick(place) }),
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailCoursePlaceEditEmptyView(
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
private fun DetailCourseScreenPreview() {
    MeongTheme {
        DetailCourseScreen(
            paddingValues = PaddingValues(),
            uiState = rememberDetailCourseUiState(),
            editActions = remember {
                object : DetailCourseEditActions {
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
                    ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "멍멍이 카페", grade = "A"),
                    ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "멍멍이 식당", grade = "A"),
                    ScheduleUiModel(id = "3", placeType = PlaceType.SIGHTSEEING, placeName = "멍멍이 산책길", grade = "A"),
                )
            },
            course = DetailCourseUiModel(
                name = "강릉 2박 3일 워케이션",
                region = Region.GANGNEUNG,
                startDate = "2026-08-10",
                endDate = "2026-08-12",
                totalDays = 3,
                dayItems = persistentMapOf(
                    2 to persistentListOf(
                        ScheduleUiModel(id = "4", placeType = PlaceType.ACCOMMODATION, placeName = "프렌즈애견펜션", grade = "A", location = "강원 강릉시 하남길 117-4")
                    )
                ),
                dayReturnToAccommKm = persistentMapOf(2 to 1.2)
            ),
            dayNumber = 2,
            favoritePlaceIds = persistentSetOf(),
            onFavoriteToggle = {},
            petInfo = UiState.Success(
                PetInfo(
                    id = 1,
                    name = "몽몽이",
                    breed = "푸들",
                    weightKg = 5.0,
                    birthDate = "2020-01-01",
                    isNeutered = true,
                    imageUrl = "",
                    sizeCategory = PetSizeCategory.SMALL,
                    activityLevel = PetActivityLevel.MEDIUM,
                    sociability = PetSociability.NORMAL,
                    healthStatus = PetHealthStatus.RECENT_TREATMENT
                )
            ),
            onRetryPetInfo = {},
            onPreviousDayClick = {},
            onNextDayClick = {},
            onBackClick = {},
            onChangeScheduleItemClick = {},
            onDeleteScheduleItemClick = {}
        )
    }
}
