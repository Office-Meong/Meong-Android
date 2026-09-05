package com.office.meong.data.review.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    @SerialName("score")
    val score: Int,
    @SerialName("content")
    val content: String,
)
