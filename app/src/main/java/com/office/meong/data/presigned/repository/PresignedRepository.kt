package com.office.meong.data.presigned.repository

interface PresignedRepository {
    suspend fun uploadImage(
        uriString: String,
        fileName: String,
        contentType: String = "image/webp"
    ): Result<String>
}
