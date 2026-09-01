package com.office.meong.presentation.course.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.office.meong.core.common.model.LoadErrorHandleAction
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.data.course.model.CourseDetail
import com.office.meong.data.course.repository.CourseRepository
import com.office.meong.data.favorite.repository.FavoriteRepository
import com.office.meong.data.pet.model.toInfo
import com.office.meong.data.pet.repository.PetRepository
import com.office.meong.data.place.model.PlacePage
import com.office.meong.domain.favorite.usecase.ToggleFavoriteUseCase
import com.office.meong.domain.place.model.PlaceSearchQuery
import com.office.meong.domain.place.usecase.PlaceSearchUseCase
import com.office.meong.presentation.course.detail.model.DetailCourseUiModel
import com.office.meong.presentation.course.model.ScheduleUiModel
import com.office.meong.presentation.course.model.accommodationForDay
import com.office.meong.presentation.course.model.toScheduleUiModel
import com.office.meong.presentation.course.detail.model.toUiModel
import com.office.meong.presentation.course.detail.navigation.DetailCourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val courseRepository: CourseRepository,
    private val petRepository: PetRepository,
    private val favoriteRepository: FavoriteRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val placeSearchUseCase: PlaceSearchUseCase,
) : ViewModel() {
    private val courseId = savedStateHandle.toRoute<DetailCourse>().courseId

    private val _state = MutableStateFlow(DetailCourseState())
    val state: StateFlow<DetailCourseState> = _state.asStateFlow()

    private val _sideEffect = Channel<DetailCourseSideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    // 같은 검색어를 다시 입력해도 그대로 방출되도록 StateFlow 대신 SharedFlow 를 쓴다.
    private val placeSearchQueries = MutableSharedFlow<PlaceSearchQuery>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    init {
        fetchCourseDetail()
        fetchPetInfo()
        fetchFavoritePlaces()

        viewModelScope.launch {
            // 실제 검색어가 입력됐을 때만 조회한다(상세 화면 진입 시 빈 검색 요청 방지)
            placeSearchUseCase.search(placeSearchQueries.filter { !it.keyword.isNullOrBlank() })
                .collect(::handlePlaceSearchResult)
        }
    }

    fun retryCourseDetail() {
        fetchCourseDetail()
    }

    fun onFavoriteToggle(place: ScheduleUiModel) {
        val placeId = place.placeId ?: return
        val isFavorite = _state.value.favoritePlaceIds.contains(placeId)

        viewModelScope.launch {
            toggleFavoriteUseCase.toggle(placeId, isFavorite)
                .onSuccess {
                    _state.update { current ->
                        val places = when (val favorites = current.favoritePlaces) {
                            is UiState.Success -> favorites.data
                            else -> persistentListOf()
                        }
                        val updated = if (isFavorite) {
                            places.filterNot { it.placeId == placeId }
                        } else {
                            places + place
                        }.toImmutableList()

                        val favoritePlaces = if (updated.isEmpty()) UiState.Empty else UiState.Success(updated)
                        current.copy(
                            favoritePlaces = favoritePlaces,
                            favoritePlaceIds = favoritePlaces.toFavoritePlaceIds()
                        )
                    }
                }
                .onFailure {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("즐겨찾기 처리에 실패했어요"))
                }
        }
    }

    fun fetchFavoritePlaces() {
        viewModelScope.launch {
            favoriteRepository.getFavorites()
                .onSuccess { favorites ->
                    _state.update {
                        val favoritePlaces = if (favorites.isEmpty()) {
                            UiState.Empty
                        } else {
                            UiState.Success(favorites.map { favorite -> favorite.toScheduleUiModel() }.toImmutableList())
                        }
                        it.copy(
                            favoritePlaces = favoritePlaces,
                            favoritePlaceIds = favoritePlaces.toFavoritePlaceIds()
                        )
                    }
                }
                .onFailure {
                    // 실패 시 이전에 캐싱된 favoritePlaceIds는 그대로 유지한다.
                    _state.update { it.copy(favoritePlaces = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun retryPetInfo() {
        fetchPetInfo()
    }

    fun selectPreviousDay() {
        _state.update { it.copy(selectedDayNumber = (it.selectedDayNumber - 1).coerceAtLeast(1)) }
    }

    fun selectNextDay() {
        val totalDays = _state.value.course.successData?.totalDays ?: return
        _state.update {
            it.copy(
                selectedDayNumber = (it.selectedDayNumber + 1).coerceAtMost(
                    totalDays
                )
            )
        }
    }

    fun removeCourse() {
        viewModelScope.launch {
            courseRepository.deleteCourse(
                courseId = courseId
            ).onSuccess {
                _sideEffect.send(DetailCourseSideEffect.ShowToast("코스를 삭제했어요"))
                _sideEffect.send(DetailCourseSideEffect.NavigateUp)
            }.onFailure {
                _sideEffect.send(DetailCourseSideEffect.ShowToast("코스 삭제에 실패했어요"))
            }
        }
    }

    fun onPlaceSearchQueryChanged(keyword: String) {
        if (keyword.isBlank()) {
            _state.update { it.copy(placeSearchResults = UiState.Empty) }
            return
        }

        _state.update { it.copy(placeSearchResults = UiState.Loading) }
        placeSearchQueries.tryEmit(
            PlaceSearchQuery(
                keyword = keyword,
                region = _state.value.course.successData?.region,
                page = 0,
            )
        )
    }

    private fun handlePlaceSearchResult(result: Result<PlacePage>) {
        result
            .onSuccess { page ->
                val places = page.content.map { it.toScheduleUiModel() }.toImmutableList()
                _state.update {
                    it.copy(placeSearchResults = if (places.isEmpty()) UiState.Empty else UiState.Success(places))
                }
            }
            .onFailure {
                _state.update { it.copy(placeSearchResults = UiState.Failure(LoadErrorHandleAction.Retry)) }
            }
    }

    fun addCourseItem(dayNumber: Int, placeId: Long) {
        viewModelScope.launch {
            courseRepository.addCourseItem(
                courseId = courseId,
                dayNumber = dayNumber,
                placeId = placeId
            )
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(mapCourseToUiModel(course))) }
                }
                .onFailure {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("장소 추가에 실패했어요"))
                }
        }
    }

    fun fetchAccommodationAlternatives() {
        val itemId = accommodationItemId() ?: return

        viewModelScope.launch {
            _state.update { it.copy(accommodationAlternatives = UiState.Loading) }

            courseRepository.getCourseItemAlternatives(courseId, itemId)
                .onSuccess { alternatives ->
                    _state.update {
                        it.copy(
                            accommodationAlternatives = UiState.Success(
                                alternatives.map { place -> place.toScheduleUiModel() }.toImmutableList()
                            )
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(accommodationAlternatives = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun selectAccommodationAlternative(place: ScheduleUiModel) {
        val itemId = accommodationItemId() ?: return
        val placeId = place.placeId ?: return

        viewModelScope.launch {
            courseRepository.updateCourseItem(
                courseId = courseId,
                itemId = itemId,
                startTime = null,
                endTime = null,
                newPlaceId = placeId
            )
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(mapCourseToUiModel(course))) }
                }
                .onFailure {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("숙소 변경에 실패했어요"))
                }
        }
    }

    /**
     * 백엔드가 주소를 안 준 항목(주로 도보 코스형)은 location 이 빈 값으로 남는다.
     * UI 에서 "카카오맵으로 확인해요" 안내로 대체 노출한다.
     */
    private fun mapCourseToUiModel(course: CourseDetail): DetailCourseUiModel = course.toUiModel()

    private fun accommodationItemId(): Long? {
        val state = _state.value
        return state.course.successData?.dayItems?.accommodationForDay(state.selectedDayNumber)?.id?.toLongOrNull()
    }

    fun fetchScheduleItemAlternatives(itemId: Long) {
        viewModelScope.launch {
            _state.update { it.copy(scheduleItemAlternatives = UiState.Loading, editingScheduleItemId = itemId) }

            courseRepository.getCourseItemAlternatives(courseId, itemId)
                .onSuccess { alternatives ->
                    _state.update {
                        it.copy(
                            scheduleItemAlternatives = UiState.Success(
                                alternatives.map { place -> place.toScheduleUiModel() }.toImmutableList()
                            )
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(scheduleItemAlternatives = UiState.Failure(LoadErrorHandleAction.Retry)) }
                }
        }
    }

    fun selectScheduleItemAlternative(place: ScheduleUiModel) {
        val itemId = _state.value.editingScheduleItemId ?: return
        val placeId = place.placeId ?: return

        viewModelScope.launch {
            courseRepository.updateCourseItem(
                courseId = courseId,
                itemId = itemId,
                startTime = null,
                endTime = null,
                newPlaceId = placeId
            )
                .onSuccess { course ->
                    _state.update {
                        it.copy(
                            course = UiState.Success(mapCourseToUiModel(course)),
                            editingScheduleItemId = null,
                        )
                    }
                }
                .onFailure { _sideEffect.send(DetailCourseSideEffect.ShowToast("장소 변경에 실패했어요")) }
        }
    }

    fun updateCourseName(name: String) {
        viewModelScope.launch {
            courseRepository.updateCourseName(courseId, name)
                .onSuccess { course ->
                    _state.update { it.copy(course = UiState.Success(mapCourseToUiModel(course))) }
                }
                .onFailure {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("코스 이름 변경에 실패했어요"))
                }
        }
    }

    /**
     * 일정 편집을 완료할 때 호출한다. 편집 중 삭제된 아이템은 로컬에서만 제거해두고
     * 여기서 한 번에 서버 삭제를 처리한다. 삭제/추가 API는 서버에서 이미 재정렬까지
     * 마치므로, 사용자가 드래그로 순서를 직접 바꾼 경우(reorderNeeded)에만 순서 변경
     * API를 추가로 호출한다 — 아니면 방금 추가/삭제만 한 아이템 구성과 어긋나 순서
     * 변경 요청이 불필요하게 실패할 수 있다.
     */
    fun completeScheduleEdit(dayNumber: Int, deletedItemIds: List<Long>, itemIds: List<Long>, reorderNeeded: Boolean) {
        viewModelScope.launch {
            var latestCourse: CourseDetail? = null
            for (itemId in deletedItemIds) {
                val result = courseRepository.deleteCourseItem(courseId, itemId)
                if (result.isFailure) {
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("장소 삭제에 실패했어요"))
                    return@launch
                }
                latestCourse = result.getOrNull()
            }

            if (reorderNeeded) {
                courseRepository.reorderCourseItems(courseId, dayNumber, itemIds)
                    .onSuccess { course ->
                        _state.update { it.copy(course = UiState.Success(mapCourseToUiModel(course))) }
                    }
                    .onFailure {
                        _sideEffect.send(DetailCourseSideEffect.ShowToast("일정 순서 변경에 실패했어요"))
                    }
            } else {
                latestCourse?.let { course ->
                    _state.update { it.copy(course = UiState.Success(mapCourseToUiModel(course))) }
                }
            }
        }
    }

    private fun fetchCourseDetail() {
        viewModelScope.launch {
            _state.update { it.copy(course = UiState.Loading) }

            courseRepository.getDetailCourse(courseId)
                .onSuccess { course ->
                    _state.update {
                        it.copy(
                            course = UiState.Success(mapCourseToUiModel(course)),
                            selectedDayNumber = 1
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(course = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("코스 정보를 불러오지 못했어요"))
                }
        }
    }

    private fun fetchPetInfo() {
        viewModelScope.launch {
            _state.update { it.copy(petInfo = UiState.Loading) }

            petRepository.getDogs()
                .onSuccess { pets ->
                    _state.update { currentState ->
                        currentState.copy(
                            petInfo = pets.firstOrNull()?.toInfo()
                                ?.let { UiState.Success(it) }
                                ?: UiState.Empty
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(petInfo = UiState.Failure(LoadErrorHandleAction.Retry)) }
                    _sideEffect.send(DetailCourseSideEffect.ShowToast("반려견 정보를 불러오지 못했어요"))
                }
        }
    }
}
