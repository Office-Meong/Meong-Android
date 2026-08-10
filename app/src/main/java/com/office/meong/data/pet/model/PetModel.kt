package com.office.meong.data.pet.model

import com.office.meong.data.pet.remote.dto.request.DogRequest
import com.office.meong.data.pet.remote.dto.response.DogResponse

data class PetModel(
    val id: Long,
    val name: String,
    val breed: String,
    val weightKg: Double,
    val birthDate: String,
    val isNeutered: Boolean,
    val imageUrl: String,
    val sizeCategory: String,
    val activityLevel: String,
    val sociability: String,
    val healthStatus: String,
) {
    fun toDto() = DogRequest(
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
}

fun DogResponse.toModel() = PetModel(
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
