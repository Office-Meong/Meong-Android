package com.office.meong.presentation.auth.model

data class SignUpUiState(
    val imageUrl: String? = null,
    val petName: String = "",
    val selectedSize: PetSize? = null,
    val selectedActivity: PetActivity? = null,
    val selectedSociability: PetSociability? = null,
    val selectedHealth: PetHealth? = null
) {
    val isSaveEnabled: Boolean
        get() = petName.isNotBlank() &&
                selectedSize != null &&
                selectedActivity != null &&
                selectedSociability != null &&
                selectedHealth != null
}