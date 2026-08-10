package com.office.meong.presentation.mypage.petedit.model

data class PetEditUiState(
    val imageUrl: String? = null,
    val petName: String = "",
    val breed: String = "",
    val weightKg: String = "",
    val birthDate: String = "",
    val isNeutered: Boolean = false,
    val selectedSize: PetSize? = null,
    val selectedActivity: PetActivity? = null,
    val selectedSociability: PetSociability? = null,
    val selectedHealth: PetHealth? = null
) {
    val isSaveEnabled: Boolean
        get() = petName.isNotBlank() &&
                breed.isNotBlank() &&
                weightKg.toDoubleOrNull() != null &&
                birthDate.isNotBlank() &&
                selectedSize != null &&
                selectedActivity != null &&
                selectedSociability != null &&
                selectedHealth != null
}
