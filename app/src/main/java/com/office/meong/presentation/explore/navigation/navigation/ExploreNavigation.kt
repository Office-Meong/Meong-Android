package com.office.meong.presentation.explore.navigation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.presentation.explore.navigation.ExploreRoute
import kotlinx.serialization.Serializable

@Serializable
data object Explore: MainTabRoute

fun NavController.navigateToExplore(navOptions: NavOptions? = null) =
    navigate(Explore, navOptions)

fun NavGraphBuilder.exploreNavGraph(
    paddingValues: PaddingValues
) {
    composable<Explore> {
        ExploreRoute(
            paddingValues = paddingValues
        )
    }
}