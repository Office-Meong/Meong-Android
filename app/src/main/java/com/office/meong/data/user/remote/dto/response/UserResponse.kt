package com.office.meong.data.user.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("nickname")
    val nickname: String = "",
    @SerialName("profileImageUrl")
    val profileImageUrl: String? = null,
    @SerialName("email")
    val email: String? = null,
    @SerialName("createdAt")
    val createdAt: String = ""
)
