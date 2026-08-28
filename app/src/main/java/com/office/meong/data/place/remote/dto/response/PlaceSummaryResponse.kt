package com.office.meong.data.place.remote.dto.response

import com.office.meong.core.model.region.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 서버가 일부 레코드에서 문자열·숫자 필드에 null 을 내려보내므로(예: acmpyType),
 * id·name 을 제외한 값에는 기본값을 두어 [kotlinx.serialization.json.Json.coerceInputValues]
 * 가 null 을 기본값으로 대체하도록 한다. 한 항목의 null 때문에 페이지 전체 파싱이
 * 깨지지 않게 하기 위함이다.
 */
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
