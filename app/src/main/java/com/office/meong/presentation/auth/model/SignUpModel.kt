package com.office.meong.presentation.auth.model

import com.office.meong.core.designsystem.component.textfield.toIsoBirthDateOrNull
import com.office.meong.data.pet.model.PetInputModel

fun SignUpState.toPetInputModel() = PetInputModel(
    name = nameTextFieldState.text.toString(),
    breed = breedTextFieldState.text.toString().trim().ifBlank { null },
    weightKg = weightTextFieldState.text.toString().toDoubleOrNull(),
    birthDate = birthDateTextFieldState.text.toString().toIsoBirthDateOrNull(),
    isNeutered = isNeutered,
    imageUrl = imageUrl,
    sizeCategory = requireNotNull(selectedSize),
    activityLevel = requireNotNull(selectedActivity),
    sociability = requireNotNull(selectedSociability),
    healthStatus = requireNotNull(selectedHealth),
)
