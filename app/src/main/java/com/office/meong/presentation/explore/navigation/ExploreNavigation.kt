package com.office.meong.presentation.explore.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Explore: MainTabRoute

fun NavHostController.navigateToExplore(navOptions: NavOptions? = null) {
    navigate(Explore, navOptions)
}