package com.office.meong.data.review.model

import com.office.meong.data.review.remote.dto.response.ReviewResponse

data class ReviewModel(
    val id: Long,
    val userId: Long,
    val userNickname: String,
    val score: Int,
    val content: String,
    val createdAt: String,
)

fun ReviewResponse.toModel(): ReviewModel = ReviewModel(
    id = id,
    userId = userId,
    userNickname = userNickname,
    score = score,
    content = content,
    createdAt = createdAt,
)
