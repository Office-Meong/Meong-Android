package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacePetConditionResponse(
    @SerialName("acmpyType")
    val acmpyType: String,
    @SerialName("isCageRequired")
    val isCageRequired: Boolean,
    @SerialName("isLeashRequired")
    val isLeashRequired: Boolean,
    @SerialName("petWeightLimitKg")
    val petWeightLimitKg: Double?,
    @SerialName("catAllowed")
    val catAllowed: Boolean,
    @SerialName("bathAvailable")
    val bathAvailable: Boolean,
    @SerialName("companionConditions")
    val companionConditions: String?,
    @SerialName("availableFacilities")
    val availableFacilities: String?,
    @SerialName("cautions")
    val cautions: String?,
)
