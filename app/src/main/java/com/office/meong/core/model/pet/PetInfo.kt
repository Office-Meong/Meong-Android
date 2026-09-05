package com.office.meong.core.model.pet

data class PetInfo(
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
)
