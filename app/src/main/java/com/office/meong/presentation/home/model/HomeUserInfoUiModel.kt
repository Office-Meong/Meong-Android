package com.office.meong.presentation.home.model

import androidx.compose.runtime.Immutable
import com.office.meong.data.user.model.UserInfoModel

@Immutable
data class HomeUserInfoUiModel(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String,
    val email: String,
    val createdAt: String,
)

fun UserInfoModel.toUiModel() = HomeUserInfoUiModel(
    id = id,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    email = email,
    createdAt = createdAt,
)
