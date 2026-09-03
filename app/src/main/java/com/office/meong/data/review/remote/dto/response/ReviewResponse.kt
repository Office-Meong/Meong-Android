package com.office.meong.data.review.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("userId")
    val userId: Long = 0L,
    @SerialName("userNickname")
    val userNickname: String = "",
    @SerialName("score")
    val score: Int = 0,
    @SerialName("content")
    val content: String = "",
    @SerialName("createdAt")
    val createdAt: String = "",
)
