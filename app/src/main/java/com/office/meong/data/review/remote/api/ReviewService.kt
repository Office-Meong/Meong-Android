package com.office.meong.data.review.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.review.remote.dto.request.ReviewRequest
import com.office.meong.data.review.remote.dto.response.ReviewResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {
    @GET("places/{placeId}/reviews")
    suspend fun getReviews(
        @Path("placeId") placeId: Long
    ): BaseResponse<List<ReviewResponse>>

    @POST("places/{placeId}/reviews")
    suspend fun postReview(
        @Path("placeId") placeId: Long,
        @Body reviewRequest: ReviewRequest,
    ): BaseResponse<ReviewResponse>

    @DELETE("reviews/{reviewId}")
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Long
    ): BaseResponse<Unit>
}
