package com.office.meong.presentation.auth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.SignUpRoute
import kotlinx.serialization.Serializable

@Serializable
data object Signup: Route

fun NavController.navigateToSignup(navOptions: NavOptions? = null) =
    navigate(Signup, navOptions)

fun NavGraphBuilder.signupNavGraph(
    paddingValues: PaddingValues
) {
    composable<Signup> {
        SignUpRoute(
            paddingValues = paddingValues
        )
    }
}
