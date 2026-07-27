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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.office.meong.R
import com.office.meong.core.common.extension.disableNestedScroll
import com.office.meong.core.common.extension.disableUpWardEvent
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.statusBarColor
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
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
import com.office.meong.presentation.course.result.model.CurrentDialogType
import com.office.meong.presentation.course.result.model.PlaceEditChipType
import com.office.meong.presentation.course.result.model.RouteIndicatorType
import com.office.meong.presentation.course.result.model.ScheduleUiModel
import com.office.meong.presentation.course.result.model.ScheduleUiModel.Companion.DUMMY_SEARCHABLE_PLACES
import com.office.meong.presentation.course.result.state.rememberScheduleDragDropState
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
    navigateUp: () -> Unit
) {
    var currentDialogType by remember { mutableStateOf<CurrentDialogType?>(null) }
    var isEditTitle by remember { mutableStateOf(false) }
    var isEditSchedule by remember { mutableStateOf(false) }
    var isEditAccommodation by remember { mutableStateOf(false) }
    var editPlaceChipType by remember { mutableStateOf(PlaceEditChipType.SEARCH) }
    var isScheduleLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(10.seconds)
        isScheduleLoading = false
    }

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
        isScheduleLoading = isScheduleLoading,
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

    if (isEditAccommodation) {
        EditPlaceBottomSheet(
            selectedChipType = editPlaceChipType,
            onChipClick = { editPlaceChipType = it },
            onDismiss = {
                isEditAccommodation = false
                editPlaceChipType = PlaceEditChipType.SEARCH
            }
        )
    }
}

@Composable
private fun ResultCourseScreen(
    paddingValues: PaddingValues,
    scheduleUiModels: SnapshotStateList<ScheduleUiModel>,
    isEditSchedule: Boolean,
    isScheduleLoading: Boolean,
    editActions: ResultCourseEditActions,
    onBackClick: () -> Unit,
    removeCurrentRoute: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current
    val scheduleShimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = meongShimmerTheme()
    )

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
                        isLoading = isScheduleLoading,
                        shimmer = scheduleShimmer,
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
                onEditTitle(false)
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
 * TODO: 서버 API 구조 확정 후 실제 검색/관심 장소 데이터 연동으로 교체
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditPlaceBottomSheet(
    selectedChipType: PlaceEditChipType,
    onChipClick: (PlaceEditChipType) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeShimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View,
        theme = meongShimmerTheme()
    )

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

                    MeongTextField(
                        state = searchFieldState,
                        placeholder = "장소를 검색해주세요",
                        leadingIcon = R.drawable.ic_search,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (query.isNotBlank()) {
                        PlaceEditResultList(
                            isLoading = isSearchLoading,
                            places = searchResults,
                            shimmer = placeShimmer,
                            emptyTitle = "검색 결과가 없어요",
                            emptyDescription = "다른 검색어를 입력해주세요",
                            onFavoriteClick = {},
                            modifier = Modifier
                                .weight(1f)
                        )
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
                        modifier = Modifier
                            .weight(1f)
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
private fun PlaceEditResultList(
    isLoading: Boolean,
    places: ImmutableList<ScheduleUiModel>,
    shimmer: Shimmer,
    emptyTitle: String,
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    emptyDescription: String? = null,
) {
    when {
        isLoading -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = PLACE_SKELETON_ITEM_COUNT) {
                    MeongPlaceCardSkeleton(shimmer = shimmer)
                }
            }
        }

        places.isEmpty() -> {
            PlaceEditEmptyView(
                title = emptyTitle,
                description = emptyDescription,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 54.dp)
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
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
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f),
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
            scheduleUiModels = remember {
                mutableStateListOf(
                    ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "프렌즈애견펜션", grade = "A"),
                    ScheduleUiModel(id = "3", placeType = PlaceType.OTHER, placeName = "프렌즈애견펜션", grade = "A"),
                )
            },
            onBackClick = {},
            isEditSchedule = false,
            isScheduleLoading = false,
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
