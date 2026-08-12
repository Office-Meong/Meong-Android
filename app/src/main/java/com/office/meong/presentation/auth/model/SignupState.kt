package com.office.meong.presentation.auth.model

import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability

data class SignUpUiState(
    val imageUrl: String? = null,
    val petName: String = "",
    val selectedSize: PetSizeCategory? = null,
    val selectedActivity: PetActivityLevel? = null,
    val selectedSociability: PetSociability? = null,
    val selectedHealth: PetHealthStatus? = null
) {
    val isSaveEnabled: Boolean
        get() = petName.isNotBlank() &&
                selectedSize != null &&
                selectedActivity != null &&
                selectedSociability != null &&
                selectedHealth != null
}