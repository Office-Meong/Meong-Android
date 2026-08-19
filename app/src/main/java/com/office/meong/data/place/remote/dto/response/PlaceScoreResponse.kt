package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceScoreResponse(
    @SerialName("petCompanionScore")
    val petCompanionScore: Int,
    @SerialName("workcationScore")
    val workcationScore: Int,
    @SerialName("walkAccessibilityScore")
    val walkAccessibilityScore: Int,
    @SerialName("congestionScore")
    val congestionScore: Int,
    @SerialName("emergencyScore")
    val emergencyScore: Int,
    @SerialName("accessibilityScore")
    val accessibilityScore: Int,
    @SerialName("totalScore")
    val totalScore: Int,
    @SerialName("grade")
    val grade: String,
    @SerialName("congestionLevel")
    val congestionLevel: String,
)
