package com.office.meong.presentation.mypage.useredit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object MyPageUserEdit: Route

fun NavController.navigateToMyPageUserEdit(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) = navigate(
    route = MyPageUserEdit,
    navOptions = navOptions
)
