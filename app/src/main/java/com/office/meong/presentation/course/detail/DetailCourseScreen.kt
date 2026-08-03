package com.office.meong.presentation.course.detail

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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.platform.LocalConfiguration
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
import com.office.meong.R
import com.office.meong.core.common.dragdrop.rememberDragDropState
import com.office.meong.core.common.extension.disableNestedScroll
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.common.extension.statusBarColor
import com.office.meong.core.designsystem.component.bottomsheet.MeongBottomSheet
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.chip.ChipType
import com.office.meong.core.designsystem.component.chip.MeongChip
import com.office.meong.core.designsystem.component.textfield.MeongTextField
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.component.topbar.TopbarAction
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.place.PlaceType
import com.office.meong.presentation.course.detail.action.AccommodationEditActions
import com.office.meong.presentation.course.detail.action.DetailCourseEditActions
import com.office.meong.presentation.course.detail.action.ScheduleEditActions
import com.office.meong.presentation.course.detail.action.TitleEditActions
import com.office.meong.presentation.course.detail.component.DetailCourseAccommodationSection
import com.office.meong.presentation.course.detail.component.DetailCourseEditScheduleItem
import com.office.meong.presentation.course.detail.component.DetailCourseInfoHolder
import com.office.meong.presentation.course.detail.component.DetailCoursePlaceSummaryItem
import com.office.meong.presentation.course.detail.component.DetailCourseRouteIndicator
import com.office.meong.presentation.course.detail.component.DetailCourseScheduleItem
import com.office.meong.presentation.course.detail.component.DetailCourseScheduleSection
import com.office.meong.presentation.course.detail.component.DetailCourseTopAction
import com.office.meong.presentation.course.detail.model.DetailCourseRouteIndicatorType
import com.office.meong.presentation.course.detail.model.PlaceEditChipType
import com.office.meong.presentation.course.detail.model.ScheduleUiModel
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
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCourseRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit = {}
) {
    val uiState = rememberDetailCourseUiState()

    val scheduleUiModels = remember {
        mutableStateListOf(
            ScheduleUiModel(id = "1", placeType = PlaceType.WORKSPACE, placeName = "멍멍이 카페", grade = "A"),
            ScheduleUiModel(id = "2", placeType = PlaceType.RESTAURANT, placeName = "멍멍이 식당", grade = "A"),
            ScheduleUiModel(id = "3", placeType = PlaceType.SIGHTSEEING, placeName = "멍멍이 산책길", grade = "A"),
            ScheduleUiModel(id = "4", placeType = PlaceType.WORKSPACE, placeName = "멍멍이 오피스", grade = "A"),
            ScheduleUiModel(id = "5", placeType = PlaceType.RESTAURANT, placeName = "멍멍이 기사식당", grade = "A"),
        )
    }

    val editActions = remember(uiState) {
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
                }
            }
        }
    }

    DetailCourseScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        editActions = editActions,
        scheduleUiModels = scheduleUiModels,
        onBackClick = navigateUp
    )

    if (uiState.isEditTitleVisible) {
        DetailCourseEditTitleBottomSheet(
            onDismiss = editActions.title::onClickComplete
        )
    }

    if (uiState.isEditAccommodationVisible) {
        DetailCourseEditPlaceBottomSheet(
            selectedChipType = uiState.editPlaceChipType,
            onChipClick = uiState::selectPlaceEditChip,
            onDismiss = editActions.accommodation::onClickComplete
        )
    }
}

@Composable
private fun DetailCourseScreen(
    paddingValues: PaddingValues,
    uiState: DetailCourseUiState,
    editActions: DetailCourseEditActions,
    scheduleUiModels: SnapshotStateList<ScheduleUiModel>,
    onBackClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    // 팝업이 닫혀있는 동안엔 리컴포지션을 유발하지 않도록, 일반 배열에 최신 좌표만 계속 갱신해둔다.
    val latestMoreButtonCoordinates = remember { arrayOfNulls<LayoutCoordinates>(1) }
    var moreButtonCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

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
                .statusBarColor(backgroundColor = MeongTheme.colors.white)
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
                        location = "강릉",
                        tripDay = "2박 3일 (2026.8.10 - 2026.8.12)",
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

                    PetProfileCard(
                        petName = "몽몽이",
                        imageUrl = "",
                        tags = persistentListOf("소형견", "활동량 보통", "사회성 보통", "최근 수술, 치료중"),
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(24.dp))

                    DetailCourseAccommodationSection(
                        onChangeAccommodationClick = editActions.accommodation::onClickEdit,
                        onFavoriteClick = {},
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

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
                        dayNumber = "2",
                        tripDay = "8.11",
                        onPreviousClick = {},
                        onNextClick = {},
                        onRouteClick = {},
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
                        DetailCourseScheduleItem(
                            count = index + 1,
                            placeName = item.placeName,
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
                    Spacer(Modifier.height(10.dp))

                    DetailCourseRouteIndicator(
                        routeLength = "1.2",
                        onRouteClick = {},
                        routeIndicatorType = DetailCourseRouteIndicatorType.END,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    DetailCoursePlaceSummaryItem(
                        placeType = "숙소",
                        placeName = "프렌즈애견펜션",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(Modifier.height(40.dp))
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
                properties = PopupProperties(focusable = true)
            ) {
                DetailCourseTopAction(
                    onClick = {
                        uiState.hideTopAction()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailCourseEditTitleBottomSheet(
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
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * TODO: 서버 API 구조 확정 후 실제 검색/관심 장소 데이터 연동으로 교체
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailCourseEditPlaceBottomSheet(
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
                        searchResults = ScheduleUiModel.DUMMY_SEARCHABLE_PLACES.filter { it.placeName.contains(query) }.toPersistentList()
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
                        DetailCoursePlaceEditResultList(
                            isLoading = isSearchLoading,
                            places = searchResults,
                            shimmer = placeShimmer,
                            emptyTitle = "검색 결과가 없어요",
                            emptyDescription = "다른 검색어를 입력해주세요",
                            onFavoriteClick = {}
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

                    DetailCoursePlaceEditResultList(
                        isLoading = isFavoriteLoading,
                        places = favoritePlaces,
                        shimmer = placeShimmer,
                        emptyTitle = "저장된 관심 장소가 없어요",
                        emptyDescription = "마음에 드는 워케이션 장소를 탐색해 보세요! ",
                        onFavoriteClick = {}
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
    onFavoriteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    emptyDescription: String? = null,
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
            onBackClick = {}
        )
    }
}
