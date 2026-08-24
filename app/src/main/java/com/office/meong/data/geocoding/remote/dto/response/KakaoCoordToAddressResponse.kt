package com.office.meong.data.geocoding.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoCoordToAddressResponse(
    @SerialName("documents")
    val documents: List<KakaoAddressDocumentResponse> = emptyList(),
)

@Serializable
data class KakaoAddressDocumentResponse(
    @SerialName("road_address")
    val roadAddress: KakaoAddressDetailResponse? = null,
    @SerialName("address")
    val address: KakaoAddressDetailResponse? = null,
)

@Serializable
data class KakaoAddressDetailResponse(
    @SerialName("address_name")
    val addressName: String,
)
