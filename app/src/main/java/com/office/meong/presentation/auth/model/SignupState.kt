package com.office.meong.presentation.auth.model

import androidx.compose.foundation.text.input.TextFieldState
import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability

data class SignUpUiState(
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
    val hasAttemptedSave: Boolean = false,
) {
    val isSaveEnabled: Boolean
        get() = !isSaving &&
            !isImageUploading &&
            nameTextFieldState.text.isNotBlank() &&
            selectedSize != null &&
            selectedActivity != null &&
            selectedSociability != null &&
            selectedHealth != null

    val nameErrorMessage: String?
        get() = if (hasAttemptedSave && nameTextFieldState.text.isBlank()) {
            "반려견 이름을 입력해주세요"
        } else {
            null
        }
}

sealed interface SignUpSideEffect {
    data object NavigateToHome : SignUpSideEffect
    data class ShowSnackBar(val message: String) : SignUpSideEffect
}
