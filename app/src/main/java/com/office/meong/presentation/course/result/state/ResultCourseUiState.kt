package com.office.meong.presentation.course.result.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.office.meong.presentation.course.result.model.CurrentDialogType
import com.office.meong.presentation.course.result.model.PlaceEditChipType

@Composable
fun rememberResultCourseUiState(): ResultCourseUiState =
    remember { ResultCourseUiState.create() }

@Stable
class ResultCourseUiState private constructor() {
    var isEditTitleVisible by mutableStateOf(false)
        private set

    var isEditAccommodationVisible by mutableStateOf(false)
        private set

    var isEditSchedule by mutableStateOf(false)
        private set

    var isSaved by mutableStateOf(false)
        private set

    var currentDialogType by mutableStateOf<CurrentDialogType?>(null)
        private set

    var editPlaceChipType by mutableStateOf(PlaceEditChipType.SEARCH)
        private set

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

    fun selectPlaceEditChip(chipType: PlaceEditChipType) {
        editPlaceChipType = chipType
    }

    fun showEditSchedule() {
        isEditSchedule = true
    }

    fun hideEditSchedule() {
        isEditSchedule = false
    }

    fun showExitDialog(type: CurrentDialogType) {
        currentDialogType = type
    }

    fun hideExitDialog() {
        currentDialogType = null
    }

    fun markSaved() {
        isSaved = true
    }

    companion object {
        fun create(): ResultCourseUiState = ResultCourseUiState()
    }
}
