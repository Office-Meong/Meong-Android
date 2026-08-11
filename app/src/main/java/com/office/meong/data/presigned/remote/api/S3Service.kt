package com.office.meong.data.presigned.remote.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

interface S3Service {
    @PUT
    suspend fun uploadImageToS3(
        @Url presignedUrl: String,
        @Body image: RequestBody
    ): Response<Unit>
}
