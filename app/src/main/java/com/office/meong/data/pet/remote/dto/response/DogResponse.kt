package com.office.meong.data.pet.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String = "",
    @SerialName("breed")
    val breed: String? = null,
    @SerialName("weightKg")
    val weightKg: Double? = null,
    @SerialName("birthDate")
    val birthDate: String? = null,
    @SerialName("isNeutered")
    val isNeutered: Boolean? = null,
    @SerialName("imageUrl")
    val imageUrl: String? = null,
    @SerialName("sizeCategory")
    val sizeCategory: String = "",
    @SerialName("activityLevel")
    val activityLevel: String = "",
    @SerialName("sociability")
    val sociability: String = "",
    @SerialName("healthStatus")
    val healthStatus: String = "",
)
