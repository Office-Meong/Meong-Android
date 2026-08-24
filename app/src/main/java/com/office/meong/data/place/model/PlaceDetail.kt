package com.office.meong.data.place.model

import com.office.meong.core.model.region.Region
import com.office.meong.data.place.remote.dto.response.PlaceAccessibilityResponse
import com.office.meong.data.place.remote.dto.response.PlaceDetailResponse
import com.office.meong.data.place.remote.dto.response.PlaceOperationResponse
import com.office.meong.data.place.remote.dto.response.PlacePetConditionResponse
import com.office.meong.data.place.remote.dto.response.PlaceScoreResponse

data class PlaceScore(
    val petCompanionScore: Int,
    val workcationScore: Int,
    val walkAccessibilityScore: Int,
    val congestionScore: Int,
    val emergencyScore: Int,
    val accessibilityScore: Int,
    val totalScore: Int,
    val grade: String,
    val congestionLevel: String,
)

fun PlaceScoreResponse.toModel(): PlaceScore = PlaceScore(
    petCompanionScore = petCompanionScore,
    workcationScore = workcationScore,
    walkAccessibilityScore = walkAccessibilityScore,
    congestionScore = congestionScore,
    emergencyScore = emergencyScore,
    accessibilityScore = accessibilityScore,
    totalScore = totalScore,
    grade = grade,
    congestionLevel = congestionLevel,
)

data class PlacePetCondition(
    val acmpyType: String,
    val isCageRequired: Boolean,
    val isLeashRequired: Boolean,
    val petWeightLimitKg: Double?,
    val catAllowed: Boolean,
    val bathAvailable: Boolean,
    val companionConditions: String?,
    val availableFacilities: String?,
    val cautions: String?,
)

fun PlacePetConditionResponse.toModel(): PlacePetCondition = PlacePetCondition(
    acmpyType = acmpyType,
    isCageRequired = isCageRequired,
    isLeashRequired = isLeashRequired,
    petWeightLimitKg = petWeightLimitKg,
    catAllowed = catAllowed,
    bathAvailable = bathAvailable,
    companionConditions = companionConditions,
    availableFacilities = availableFacilities,
    cautions = cautions,
)

data class PlaceOperation(
    val operatingHours: String?,
    val closedDays: String?,
    val usageFee: String?,
    val parkingAvailable: Boolean?,
    val indoorOutdoorType: String?,
    val lodgingType: String?,
)

fun PlaceOperationResponse.toModel(): PlaceOperation = PlaceOperation(
    operatingHours = operatingHours,
    closedDays = closedDays,
    usageFee = usageFee,
    parkingAvailable = parkingAvailable,
    indoorOutdoorType = indoorOutdoorType,
    lodgingType = lodgingType,
)

data class PlaceAccessibility(
    val hasParking: Boolean,
    val strollerAccessible: Boolean,
    val hasRamp: Boolean,
    val dataAvailable: Boolean,
)

fun PlaceAccessibilityResponse.toModel(): PlaceAccessibility = PlaceAccessibility(
    hasParking = hasParking,
    strollerAccessible = strollerAccessible,
    hasRamp = hasRamp,
    dataAvailable = dataAvailable,
)

data class PlaceDetail(
    val id: Long,
    val name: String,
    val region: Region,
    val placeType: String,
    val address: String,
    val tel: String?,
    val homepage: String?,
    val overview: String?,
    val imageUrls: List<String>,
    val score: PlaceScore,
    val petCondition: PlacePetCondition,
    val operation: PlaceOperation,
    val accessibility: PlaceAccessibility,
    val favorite: Boolean,
)

fun PlaceDetailResponse.toModel(): PlaceDetail = PlaceDetail(
    id = id,
    name = name,
    region = region,
    placeType = placeType,
    address = address,
    tel = tel,
    homepage = homepage,
    overview = overview,
    imageUrls = imageUrls,
    score = score.toModel(),
    petCondition = petCondition.toModel(),
    operation = operation.toModel(),
    accessibility = accessibility.toModel(),
    favorite = favorite,
)
