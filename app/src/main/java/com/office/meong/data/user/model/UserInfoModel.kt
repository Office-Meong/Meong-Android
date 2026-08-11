package com.office.meong.data.user.model

import com.office.meong.data.user.remote.dto.response.UserResponse

data class UserInfoModel(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
    val email: String,
    val createdAt: String
)

fun UserResponse.toModel() = UserInfoModel(
    id = id,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    email = email,
    createdAt = createdAt
)
