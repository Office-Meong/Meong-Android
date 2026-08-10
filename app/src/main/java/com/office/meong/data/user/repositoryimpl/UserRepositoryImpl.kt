package com.office.meong.data.user.repositoryimpl

import com.office.meong.core.common.util.suspendRunCatching
import com.office.meong.data.user.model.UserInfoModel
import com.office.meong.data.user.model.toModel
import com.office.meong.data.user.remote.datasource.UserDataSource
import com.office.meong.data.user.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUserInfo(): Result<UserInfoModel> = suspendRunCatching {
        userDataSource.getUserInfo().toModel()
    }

    override suspend fun deleteUser(): Result<Unit> = suspendRunCatching {
        userDataSource.deleteUser()
    }

    override suspend fun patchUser(
        nickname: String,
        profileImageUrl: String,
    ): Result<UserInfoModel> = suspendRunCatching {
        userDataSource.patchUser(nickname, profileImageUrl).toModel()
    }
}
