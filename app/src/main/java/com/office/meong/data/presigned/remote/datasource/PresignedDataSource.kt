package com.office.meong.data.presigned.remote.datasource

import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.presigned.remote.api.PresignedService
import com.office.meong.data.presigned.remote.api.S3Service
import com.office.meong.data.presigned.remote.dto.request.PresignedUrlRequest
import com.office.meong.data.presigned.remote.dto.response.PresignedUrlResponse
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class PresignedDataSource @Inject constructor(
    private val presignedService: PresignedService,
    private val s3Service: S3Service,
) {
    suspend fun postPresignedUrl(fileName: String, contentType: String): PresignedUrlResponse =
        presignedService.postPresignedUrl(
            presignedUrlRequest = PresignedUrlRequest(
                fileName = fileName,
                contentType = contentType
            )
        ).getOrThrow()

    suspend fun uploadImageToS3(presignedUrl: String, image: RequestBody): Response<Unit> =
        s3Service.uploadImageToS3(presignedUrl, image)
}
