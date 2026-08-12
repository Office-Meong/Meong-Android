package com.office.meong.core.model.pet

data class PetInfo(
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