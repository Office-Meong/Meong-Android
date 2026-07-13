package com.office.meong.presentation.mypage.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object MyPage: MainTabRoute

fun NavHostController.navigateToMyPage(navOptions: NavOptions? = null) {
    navigate(MyPage, navOptions)
}