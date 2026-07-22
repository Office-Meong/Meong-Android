package com.office.meong.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.presentation.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
data object Home: MainTabRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(Home, navOptions)

fun NavGraphBuilder.homeNavGraph(
    paddingValues: PaddingValues
) {
    composable<Home> {
        HomeRoute(
            paddingValues = paddingValues
        )
    }
}
