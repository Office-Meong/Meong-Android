package com.office.meong.presentation.mypage.petedit

import androidx.compose.foundation.text.input.TextFieldState
import com.office.meong.core.common.util.UiState
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability

data class PetEditState(
    val pet: UiState<PetInfo> = UiState.Loading,
    val nameTextFieldState: TextFieldState = TextFieldState(),
    val breedTextFieldState: TextFieldState = TextFieldState(),
    val weightTextFieldState: TextFieldState = TextFieldState(),
    val birthDateTextFieldState: TextFieldState = TextFieldState(),
    val imageUrl: String? = null,
    val isImageUploading: Boolean = false,
    val isNeutered: Boolean = false,
    val selectedSize: PetSizeCategory? = null,
    val selectedActivity: PetActivityLevel? = null,
    val selectedSociability: PetSociability? = null,
    val selectedHealth: PetHealthStatus? = null,
    val isSaving: Boolean = false,
) {
    val isSaveEnabled: Boolean
        get() = pet is UiState.Success &&
            !isSaving &&
            !isImageUploading &&
            nameTextFieldState.text.isNotBlank() &&
            breedTextFieldState.text.isNotBlank() &&
            weightTextFieldState.text.toString().toDoubleOrNull() != null &&
            birthDateTextFieldState.text.isNotBlank() &&
            selectedSize != null &&
            selectedActivity != null &&
            selectedSociability != null &&
            selectedHealth != null
}

sealed interface PetEditSideEffect {
    data object NavigateUp : PetEditSideEffect
    data class ShowSnackBar(val message: String) : PetEditSideEffect
}
