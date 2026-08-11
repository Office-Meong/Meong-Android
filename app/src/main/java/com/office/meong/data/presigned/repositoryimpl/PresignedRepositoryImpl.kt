package com.office.meong.data.presigned.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.presigned.local.datasource.ImageLocalDataSource
import com.office.meong.data.presigned.remote.datasource.PresignedDataSource
import com.office.meong.data.presigned.repository.PresignedRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import javax.inject.Inject

class PresignedRepositoryImpl @Inject constructor(
    private val presignedDataSource: PresignedDataSource,
    private val imageLocalDataSource: ImageLocalDataSource,
) : PresignedRepository {

    override suspend fun uploadImage(
        uriString: String,
        fileName: String,
        contentType: String
    ): Result<String> = suspendRunCatching {
        val optimizedFile = imageLocalDataSource.getOptimizedFile(uriString)

        try {
            val requestBody = optimizedFile.asRequestBody(contentType.toMediaTypeOrNull())
            val presignedResponse = presignedDataSource.postPresignedUrl(fileName, contentType)

            val response = presignedDataSource.uploadImageToS3(presignedResponse.presignedUrl, requestBody)
            if (!response.isSuccessful) {
                error("S3 업로드 실패: ${response.code()} - ${response.errorBody()?.string()}")
            }

            imageLocalDataSource.deleteOriginalUri(uriString)
            Timber.d("S3 업로드 성공: ${presignedResponse.fileName}")
            presignedResponse.presignedUrl.substringBefore("?")
        } finally {
            optimizedFile.delete()
        }
    }
}
