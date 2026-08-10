package com.office.meong.presentation.mypage.petedit.model

import com.office.meong.data.pet.model.PetInputModel

fun PetEditUiState.toPetInputModel() = PetInputModel(
    name = petName,
    breed = breed,
    weightKg = weightKg.toDoubleOrNull() ?: 0.0,
    birthDate = birthDate,
    isNeutered = isNeutered,
    imageUrl = imageUrl.orEmpty(),
    sizeCategory = requireNotNull(selectedSize),
    activityLevel = requireNotNull(selectedActivity),
    sociability = requireNotNull(selectedSociability),
    healthStatus = requireNotNull(selectedHealth),
)
