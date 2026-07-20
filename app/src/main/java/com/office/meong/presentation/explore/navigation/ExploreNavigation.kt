package com.office.meong.presentation.explore.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Explore: MainTabRoute

fun NavController.navigateToExplore(navOptions: NavOptions? = null) =
    navigate(Explore, navOptions)