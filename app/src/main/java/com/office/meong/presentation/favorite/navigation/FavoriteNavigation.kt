package com.office.meong.presentation.favorite.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.presentation.favorite.FavoriteRoute
import kotlinx.serialization.Serializable

@Serializable
data object Favorite: MainTabRoute

fun NavController.navigateToFavorite(navOptions: NavOptions? = null) =
    navigate(Favorite, navOptions)


fun NavGraphBuilder.favoriteNavGraph(
    paddingValues: PaddingValues
) {
    composable<Favorite> {
        FavoriteRoute(
            paddingValues = paddingValues
        )
    }
}
