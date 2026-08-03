package com.office.meong.presentation.mypage.action

import androidx.compose.runtime.Stable

@Stable
interface MyPageActions {
    val info: MyPageInfoActions
    val account: MyPageAccountActions
}

@Stable
interface MyPageInfoActions {
    fun onClickTermsOfService()
    fun onClickPrivacyPolicy()
    fun onClickOpenSourceLicense()
    fun onClickFeedback()
}

@Stable
interface MyPageAccountActions {
    fun onClickLogout()
    fun onClickWithdraw()
}
