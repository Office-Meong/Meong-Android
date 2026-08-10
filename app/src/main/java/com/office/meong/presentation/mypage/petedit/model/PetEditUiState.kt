package com.office.meong.presentation.mypage.petedit.model

import com.office.meong.data.pet.model.PetActivityLevel
import com.office.meong.data.pet.model.PetHealthStatus
import com.office.meong.data.pet.model.PetSizeCategory
import com.office.meong.data.pet.model.PetSociability

data class PetEditUiState(
    val imageUrl: String? = null,
    val petName: String = "",
    val breed: String = "",
    val weightKg: String = "",
    val birthDate: String = "",
    val isNeutered: Boolean = false,
    val selectedSize: PetSizeCategory? = null,
    val selectedActivity: PetActivityLevel? = null,
    val selectedSociability: PetSociability? = null,
    val selectedHealth: PetHealthStatus? = null
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
