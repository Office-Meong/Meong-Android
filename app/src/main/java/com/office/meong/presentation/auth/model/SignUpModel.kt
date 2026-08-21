package com.office.meong.presentation.auth.model

import com.office.meong.core.designsystem.component.textfield.toIsoBirthDateOrEmpty
import com.office.meong.data.pet.model.PetInputModel

fun SignUpState.toPetInputModel() = PetInputModel(
    name = nameTextFieldState.text.toString(),
    breed = breedTextFieldState.text.toString(),
    weightKg = weightTextFieldState.text.toString().toDoubleOrNull() ?: 0.0,
    birthDate = birthDateTextFieldState.text.toString().toIsoBirthDateOrEmpty(),
    isNeutered = isNeutered,
    imageUrl = imageUrl.orEmpty(),
    sizeCategory = requireNotNull(selectedSize),
    activityLevel = requireNotNull(selectedActivity),
    sociability = requireNotNull(selectedSociability),
    healthStatus = requireNotNull(selectedHealth),
)
