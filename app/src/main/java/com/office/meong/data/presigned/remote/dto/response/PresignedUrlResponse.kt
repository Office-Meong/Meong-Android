package com.office.meong.data.presigned.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresignedUrlResponse(
    @SerialName("presignedUrl")
    val presignedUrl: String,
    @SerialName("imageUrl")
    val imageUrl: String,
)
