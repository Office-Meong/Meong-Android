package com.office.meong.data.review.remote.datasource

import com.office.meong.core.network.model.ApiException
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.review.remote.api.ReviewService
import com.office.meong.data.review.remote.dto.request.ReviewRequest
import com.office.meong.data.review.remote.dto.response.ReviewResponse
import javax.inject.Inject

class ReviewDataSource @Inject constructor(
    private val reviewService: ReviewService
) {
    suspend fun getReviews(placeId: Long): List<ReviewResponse> =
        reviewService.getReviews(placeId).getOrThrow()

    suspend fun postReview(placeId: Long, reviewRequest: ReviewRequest): ReviewResponse =
        reviewService.postReview(placeId, reviewRequest).getOrThrow()

    suspend fun deleteReview(reviewId: Long) {
        val response = reviewService.deleteReview(reviewId)
        if (!response.success) throw ApiException(response.message)
    }
}
