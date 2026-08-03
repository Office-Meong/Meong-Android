package com.office.meong.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.LoginRoute
import kotlinx.serialization.Serializable

@Serializable
data object Login: Route

fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(Login, navOptions)

fun NavGraphBuilder.loginNavGraph(
    paddingValues: PaddingValues
) {
    composable<Login> {
        LoginRoute(
            paddingValues = paddingValues
        )
    }
}
