package com.office.meong.data.place.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceSummaryResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("region")
    val region: Region = Region.UNKNOWN,
    @SerialName("placeType")
    val placeType: String = "",
    @SerialName("address")
    val address: String = "",
    @SerialName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    @SerialName("grade")
    val grade: String = "",
    @SerialName("totalScore")
    val totalScore: Int = 0,
    @SerialName("acmpyType")
    val acmpyType: String = "",
    @SerialName("congestionScore")
    val congestionScore: Int = 0,
    @SerialName("congestionLevel")
    val congestionLevel: String = "",
    @SerialName("favorite")
    val favorite: Boolean = false,
)
