package com.office.meong.presentation.mypage.licenses.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object OssLicenses: Route

fun NavController.navigateToOssLicenses(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) = navigate(
    route = OssLicenses,
    navOptions = navOptions
)