package com.office.meong.presentation.home.model

import androidx.compose.runtime.Immutable
import com.office.meong.data.pet.model.PetActivityLevel
import com.office.meong.data.pet.model.PetHealthStatus
import com.office.meong.data.pet.model.PetModel
import com.office.meong.data.pet.model.PetSizeCategory
import com.office.meong.data.pet.model.PetSociability

@Immutable
data class HomePetInfoUiModel(
    val id: Long,
    val name: String,
    val breed: String,
    val weightKg: Double,
    val birthDate: String,
    val isNeutered: Boolean,
    val imageUrl: String,
    val sizeCategory: PetSizeCategory,
    val activityLevel: PetActivityLevel,
    val sociability: PetSociability,
    val healthStatus: PetHealthStatus,
)

fun PetModel.toUiModel() = HomePetInfoUiModel(
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
