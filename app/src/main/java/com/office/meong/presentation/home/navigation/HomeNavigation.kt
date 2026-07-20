package com.office.meong.presentation.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Home: MainTabRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(Home, navOptions)