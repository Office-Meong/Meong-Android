package com.office.meong.data.review.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.review.model.ReviewModel
import com.office.meong.data.review.model.toModel
import com.office.meong.data.review.remote.datasource.ReviewDataSource
import com.office.meong.data.review.remote.dto.request.ReviewRequest
import com.office.meong.data.review.repository.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
) : ReviewRepository {
    override suspend fun getReviews(placeId: Long): Result<List<ReviewModel>> = suspendRunCatching {
        reviewDataSource.getReviews(placeId).map { it.toModel() }
    }

    override suspend fun postReview(placeId: Long, score: Int, content: String): Result<ReviewModel> =
        suspendRunCatching {
            reviewDataSource.postReview(placeId, ReviewRequest(score, content)).toModel()
        }

    override suspend fun deleteReview(reviewId: Long): Result<Unit> = suspendRunCatching {
        reviewDataSource.deleteReview(reviewId)
    }
}
