package com.office.meong.data.user.repository

import com.office.meong.data.user.model.UserInfoModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserInfo(): Result<UserInfoModel>
    fun observeUserInfo(): Flow<UserInfoModel?>
    suspend fun deleteUser(): Result<Unit>
    suspend fun patchUser(
        nickname: String,
        profileImageUrl: String
    ): Result<UserInfoModel>
}
