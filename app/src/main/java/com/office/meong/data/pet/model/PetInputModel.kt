package com.office.meong.data.pet.model

import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.data.pet.remote.dto.request.DogRequest

data class PetInputModel(
    val name: String,
    val breed: String? = null,
    val weightKg: Double? = null,
    val birthDate: String? = null,
    val isNeutered: Boolean? = null,
    val imageUrl: String? = null,
    val sizeCategory: PetSizeCategory,
    val activityLevel: PetActivityLevel,
    val sociability: PetSociability,
    val healthStatus: PetHealthStatus,
)

fun PetInputModel.toDto() = DogRequest(
    name = name,
    breed = breed?.takeIf { it.isNotBlank() },
    weightKg = weightKg,
    birthDate = birthDate?.takeIf { it.isNotBlank() },
    isNeutered = isNeutered,
    imageUrl = imageUrl?.takeIf { it.isNotBlank() },
    sizeCategory = sizeCategory.name,
    activityLevel = activityLevel.name,
    sociability = sociability.name,
    healthStatus = healthStatus.name,
)
