package com.office.meong.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.LoginRoute
import com.office.meong.presentation.home.navigation.navigateToHome
import kotlinx.serialization.Serializable

@Serializable
data object Login: Route

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(Login, navOptions)

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    composable<Login> {
        LoginRoute(
            paddingValues = paddingValues,
            navigateToHome = {
                navController.navigateToHome(
                    navOptions { popUpTo(0) { inclusive = true } }
                )
            },
            navigateToSignup = {
                navController.navigateToSignup(
                    navOptions { popUpTo(0) { inclusive = true } }
                )
            }
        )
    }
}
