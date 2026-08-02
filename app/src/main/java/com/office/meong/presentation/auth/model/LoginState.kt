package com.office.meong.presentation.auth.model

data class LoginUiState(
    val isTermsBottomSheetVisible: Boolean = false,
    val isServiceTermAgreed: Boolean = false,
    val isPrivacyTermAgreed: Boolean = false
) {
    val isSignUpEnabled: Boolean
        get() = isServiceTermAgreed && isPrivacyTermAgreed
}