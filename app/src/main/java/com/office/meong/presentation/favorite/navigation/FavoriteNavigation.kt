package com.office.meong.presentation.favorite.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.office.meong.core.navigation.MainTabRoute
import kotlinx.serialization.Serializable

@Serializable
data object Favorite: MainTabRoute

fun NavHostController.navigateToFavorite(navOptions: NavOptions? = null) {
    navigate(Favorite, navOptions)
}