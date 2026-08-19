package com.office.meong.presentation.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.navigation.navigateToLogin
import com.office.meong.presentation.home.navigation.navigateToHome
import com.office.meong.presentation.splash.SplashRoute
import kotlinx.serialization.Serializable

@Serializable
data object Splash: Route


fun NavGraphBuilder.splashNavGraph(
    navController: NavController
) {
    composable<Splash> {
        SplashRoute(
            navigateToHome = {
                navController.navigateToHome(
                    navOptions { popUpTo(Splash) { inclusive = true } }
                )
            },
            navigateToLogin = {
                navController.navigateToLogin(
                    navOptions { popUpTo(Splash) { inclusive = true } }
                )
            }
        )
    }
}
