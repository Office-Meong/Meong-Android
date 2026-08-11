package com.office.meong.data.pet.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DogRequest(
    @SerialName("name")
    val name: String,
    @SerialName("breed")
    val breed: String,
    @SerialName("weightKg")
    val weightKg: Double,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("isNeutered")
    val isNeutered: Boolean,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("sizeCategory")
    val sizeCategory: String,
    @SerialName("activityLevel")
    val activityLevel: String,
    @SerialName("sociability")
    val sociability: String,
    @SerialName("healthStatus")
    val healthStatus: String,
)
