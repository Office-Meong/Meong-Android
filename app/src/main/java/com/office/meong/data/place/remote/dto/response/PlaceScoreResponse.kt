package com.office.meong.data.place.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceScoreResponse(
    @SerialName("petCompanionScore")
    val petCompanionScore: Int = 0,
    @SerialName("workcationScore")
    val workcationScore: Int = 0,
    @SerialName("walkAccessibilityScore")
    val walkAccessibilityScore: Int = 0,
    @SerialName("congestionScore")
    val congestionScore: Int = 0,
    @SerialName("emergencyScore")
    val emergencyScore: Int = 0,
    @SerialName("accessibilityScore")
    val accessibilityScore: Int = 0,
    @SerialName("totalScore")
    val totalScore: Int = 0,
    @SerialName("grade")
    val grade: String = "",
    @SerialName("congestionLevel")
    val congestionLevel: String = "",
)
