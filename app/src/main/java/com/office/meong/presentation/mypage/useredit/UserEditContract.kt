package com.office.meong.presentation.mypage.useredit

import androidx.compose.foundation.text.input.TextFieldState
import com.office.meong.core.common.util.UiState
import com.office.meong.data.user.model.UserInfoModel

data class UserEditState(
    val user: UiState<UserInfoModel> = UiState.Loading,
    val nicknameTextFieldState: TextFieldState = TextFieldState(),
    val imageUrl: String? = null,
    val isImageUploading: Boolean = false,
    val isSaving: Boolean = false,
    val hasAttemptedSave: Boolean = false,
) {
    val isSaveEnabled: Boolean
        get() = user is UiState.Success &&
            !isSaving &&
            !isImageUploading &&
            nicknameTextFieldState.text.isNotBlank()

    val nicknameErrorMessage: String?
        get() = if (hasAttemptedSave && nicknameTextFieldState.text.isBlank()) {
            "닉네임을 입력해주세요"
        } else {
            null
        }
}

sealed interface UserEditSideEffect {
    data object NavigateUp : UserEditSideEffect
    data class ShowSnackBar(val message: String) : UserEditSideEffect
}
