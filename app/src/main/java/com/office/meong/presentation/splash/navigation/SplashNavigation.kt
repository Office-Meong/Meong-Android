package com.office.meong.presentation.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

@Serializable
data object Splash: Route


fun NavGraphBuilder.splashNavGraph(
) {
    composable<Splash> {
        SplashRoute()
    }
}
