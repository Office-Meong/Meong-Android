package com.office.meong.presentation.mypage.petedit.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object MyPagePetEdit: Route

fun NavController.navigateToMyPagePetEdit(
    navOptions: NavOptions? = navOptions {
        launchSingleTop = true
    }
) = navigate(
    route = MyPagePetEdit,
    navOptions = navOptions
)
