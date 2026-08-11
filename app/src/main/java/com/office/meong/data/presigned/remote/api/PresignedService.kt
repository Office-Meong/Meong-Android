package com.office.meong.data.presigned.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.presigned.remote.dto.request.PresignedUrlRequest
import com.office.meong.data.presigned.remote.dto.response.PresignedUrlResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface PresignedService {
    @POST("upload/presigned-url")
    suspend fun postPresignedUrl(
        @Body presignedUrlRequest: PresignedUrlRequest
    ): BaseResponse<PresignedUrlResponse>
}
