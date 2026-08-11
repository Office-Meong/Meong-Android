package com.office.meong.data.user.remote.api

import com.office.meong.core.network.model.BaseResponse
import com.office.meong.data.user.remote.dto.request.PatchUserRequest
import com.office.meong.data.user.remote.dto.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {
    @GET("users/me")
    suspend fun getUserInfo(): BaseResponse<UserResponse>

    @DELETE("users/me")
    suspend fun deleteUser(): BaseResponse<Unit>

    @PATCH("users/me")
    suspend fun patchUser(
        @Body patchUserRequest: PatchUserRequest
    ): BaseResponse<UserResponse>
}
