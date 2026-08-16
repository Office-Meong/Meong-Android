package com.office.meong.data.presigned.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlRequest(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("contentType")
    val contentType: String,
)
