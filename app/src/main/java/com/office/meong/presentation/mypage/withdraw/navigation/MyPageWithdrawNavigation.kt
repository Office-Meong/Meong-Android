package com.office.meong.presentation.mypage.withdraw.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object MyPageWithdraw : Route

fun NavController.navigateToMyPageWithdraw(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) = navigate(
    route = MyPageWithdraw,
    navOptions = navOptions
)
