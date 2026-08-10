package com.office.meong.data.user.remote.datasource

import com.office.meong.core.network.model.ApiException
import com.office.meong.core.network.model.getOrThrow
import com.office.meong.data.user.remote.api.UserService
import com.office.meong.data.user.remote.dto.request.PatchUserRequest
import com.office.meong.data.user.remote.dto.response.UserResponse
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserInfo(): UserResponse = userService.getUserInfo().getOrThrow()

    suspend fun deleteUser() {
        val response = userService.deleteUser()
        if (!response.success) throw ApiException(response.message)
    }

    suspend fun patchUser(
        nickname: String,
        profileImageUrl: String
    ): UserResponse = userService.patchUser(
        patchUserRequest = PatchUserRequest(
            nickname = nickname,
            profileImageUrl = profileImageUrl
        )
    ).getOrThrow()
}
