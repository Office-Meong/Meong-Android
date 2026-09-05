package com.office.meong.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.SignUpRoute
import com.office.meong.presentation.home.navigation.navigateToHome
import kotlinx.serialization.Serializable

@Serializable
data object Signup: Route

fun NavController.navigateToSignup(navOptions: NavOptions? = null) =
    navigate(Signup, navOptions)

fun NavGraphBuilder.signupNavGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    composable<Signup> {
        SignUpRoute(
            paddingValues = paddingValues,
            navigateToHome = {
                navController.navigateToHome(
                    navOptions { popUpTo(0) { inclusive = true } }
                )
            }
        )
    }
}
