package com.office.meong.data.pet.model

import com.office.meong.core.model.pet.PetActivityLevel
import com.office.meong.core.model.pet.PetHealthStatus
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.pet.PetSizeCategory
import com.office.meong.core.model.pet.PetSociability
import com.office.meong.data.pet.remote.dto.request.DogRequest
import com.office.meong.data.pet.remote.dto.response.DogResponse

data class PetModel(
    val id: Long,
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
) {
    fun toDto() = DogRequest(
        name = name,
        breed = breed,
        weightKg = weightKg,
        birthDate = birthDate,
        isNeutered = isNeutered,
        imageUrl = imageUrl,
        sizeCategory = sizeCategory.name,
        activityLevel = activityLevel.name,
        sociability = sociability.name,
        healthStatus = healthStatus.name,
    )
}

fun DogResponse.toModel() = PetModel(
    id = id,
    name = name,
    breed = breed,
    weightKg = weightKg,
    birthDate = birthDate,
    isNeutered = isNeutered,
    imageUrl = imageUrl,
    sizeCategory = PetSizeCategory.from(sizeCategory),
    activityLevel = PetActivityLevel.from(activityLevel),
    sociability = PetSociability.from(sociability),
    healthStatus = PetHealthStatus.from(healthStatus),
)

fun PetModel.toInfo() = PetInfo(
    id = id,
    name = name,
    breed = breed,
    weightKg = weightKg,
    birthDate = birthDate,
    isNeutered = isNeutered,
    imageUrl = imageUrl,
    sizeCategory = sizeCategory,
    activityLevel = activityLevel,
    sociability = sociability,
    healthStatus = healthStatus,
)
