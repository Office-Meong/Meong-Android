package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacePetConditionResponse(
    @SerialName("acmpyType")
    val acmpyType: String = "",
    @SerialName("isCageRequired")
    val isCageRequired: Boolean = false,
    @SerialName("isLeashRequired")
    val isLeashRequired: Boolean = false,
    @SerialName("petWeightLimitKg")
    val petWeightLimitKg: Double? = null,
    @SerialName("catAllowed")
    val catAllowed: Boolean = false,
    @SerialName("bathAvailable")
    val bathAvailable: Boolean = false,
    @SerialName("companionConditions")
    val companionConditions: String? = null,
    @SerialName("availableFacilities")
    val availableFacilities: String? = null,
    @SerialName("cautions")
    val cautions: String? = null,
)
