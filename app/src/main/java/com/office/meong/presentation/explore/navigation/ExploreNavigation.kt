package com.office.meong.presentation.explore.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.presentation.explore.ExploreRoute
import com.office.meong.presentation.explore.detail.ExploreDetailRoute
import com.office.meong.presentation.explore.detail.navigation.ExploreDetail
import com.office.meong.presentation.explore.detail.navigation.navigateToExploreDetail
import kotlinx.serialization.Serializable

@Serializable
data object ExploreGraph

@Serializable
data object Explore: MainTabRoute

fun NavController.navigateToExplore(navOptions: NavOptions? = null) =
    navigate(Explore, navOptions)

fun NavGraphBuilder.exploreNavGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation<ExploreGraph>(startDestination = Explore) {
        composable<Explore> {
            ExploreRoute(
                paddingValues = paddingValues,
                navigateToDetail = { placeId -> navController.navigateToExploreDetail(placeId) }
            )
        }

        composable<ExploreDetail> {
            ExploreDetailRoute(
                paddingValues = paddingValues,
                onBackClick = navController::navigateUp
            )
        }
    }
}
