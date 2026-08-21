package com.office.meong.data.review.repository

import com.office.meong.data.review.model.ReviewModel

interface ReviewRepository {
    suspend fun getReviews(placeId: Long): Result<List<ReviewModel>>

    suspend fun postReview(placeId: Long, score: Int, content: String): Result<ReviewModel>

    suspend fun deleteReview(reviewId: Long): Result<Unit>
}
