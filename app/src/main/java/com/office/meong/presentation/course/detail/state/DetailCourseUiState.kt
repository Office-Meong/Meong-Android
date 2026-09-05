package com.office.meong.presentation.course.detail.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.office.meong.presentation.course.detail.model.PlaceEditChipType

@Composable
fun rememberDetailCourseUiState(): DetailCourseUiState =
    remember { DetailCourseUiState.create() }

@Stable
class DetailCourseUiState private constructor() {
    var isTopActionVisible by mutableStateOf(false)
        private set

    var isEditTitleVisible by mutableStateOf(false)
        private set

    var isEditAccommodationVisible by mutableStateOf(false)
        private set

    var isAddPlaceVisible by mutableStateOf(false)
        private set

    var isEditScheduleItemVisible by mutableStateOf(false)
        private set

    var isEditSchedule by mutableStateOf(false)
        private set

    var isDeleteDialogVisible by mutableStateOf(false)
        private set

    var scheduleItemIdPendingDelete by mutableStateOf<Long?>(null)
        private set

    var editPlaceChipType by mutableStateOf(PlaceEditChipType.SEARCH)
        private set

    fun toggleTopAction() {
        isTopActionVisible = !isTopActionVisible
    }

    fun hideTopAction() {
        isTopActionVisible = false
    }

    fun showEditTitle() {
        isEditTitleVisible = true
    }

    fun hideEditTitle() {
        isEditTitleVisible = false
    }

    fun showEditAccommodation() {
        isEditAccommodationVisible = true
    }

    fun hideEditAccommodation() {
        isEditAccommodationVisible = false
        editPlaceChipType = PlaceEditChipType.SEARCH
    }

    fun showAddPlace() {
        isAddPlaceVisible = true
    }

    fun hideAddPlace() {
        isAddPlaceVisible = false
        editPlaceChipType = PlaceEditChipType.SEARCH
    }

    fun showEditScheduleItem() {
        isEditScheduleItemVisible = true
    }

    fun hideEditScheduleItem() {
        isEditScheduleItemVisible = false
        editPlaceChipType = PlaceEditChipType.SEARCH
    }

    fun selectPlaceEditChip(chipType: PlaceEditChipType) {
        editPlaceChipType = chipType
    }

    fun showEditSchedule() {
        isEditSchedule = true
    }

    fun hideEditSchedule() {
        isEditSchedule = false
    }

    fun showDeleteDialog() {
        isDeleteDialogVisible = true
    }

    fun hideDeleteDialog() {
        isDeleteDialogVisible = false
    }

    fun showDeleteScheduleItemDialog(itemId: Long) {
        scheduleItemIdPendingDelete = itemId
    }

    fun hideDeleteScheduleItemDialog() {
        scheduleItemIdPendingDelete = null
    }

    companion object {
        fun create(): DetailCourseUiState = DetailCourseUiState()
    }
}
