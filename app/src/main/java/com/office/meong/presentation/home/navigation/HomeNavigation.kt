package com.office.meong.presentation.home.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Home: MainTabRoute

fun NavHostController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(Home, navOptions)
}