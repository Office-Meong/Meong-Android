package com.office.meong.presentation.myroute.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object MyRoute: MainTabRoute

fun NavHostController.navigateToMyRoute(navOptions: NavOptions? = null) {
    navigate(MyRoute, navOptions)
}