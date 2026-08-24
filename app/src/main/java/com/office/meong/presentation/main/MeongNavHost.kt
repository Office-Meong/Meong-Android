package com.office.meong.presentation.main

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.office.meong.presentation.auth.navigation.loginNavGraph
import com.office.meong.presentation.auth.navigation.signupNavGraph
import com.office.meong.presentation.course.create.navigation.navigateToCreateCourse
import com.office.meong.presentation.course.detail.navigation.navigateToDetailCourse
import com.office.meong.presentation.course.navigation.courseNavGraph
import com.office.meong.presentation.explore.navigation.exploreNavGraph
import com.office.meong.presentation.favorite.navigation.favoriteNavGraph
import com.office.meong.presentation.home.navigation.homeNavGraph
import com.office.meong.presentation.main.state.MainAppState
import com.office.meong.presentation.mypage.navigation.myPageNavGraph
import com.office.meong.presentation.splash.navigation.splashNavGraph

private const val NavigationAnimationDurationMillis = 280

@Composable
fun MeongNavHost(
    paddingValues: PaddingValues,
    appState: MainAppState,
) {
    NavHost(
        navController = appState.navController,
        startDestination = appState.startDestination,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(NavigationAnimationDurationMillis, easing = FastOutSlowInEasing),
                initialOffsetX = { it / 3 }
            ) + fadeIn(tween(NavigationAnimationDurationMillis))
        },
        exitTransition = {
            slideOutHorizontally(
                animationSpec = tween(NavigationAnimationDurationMillis, easing = FastOutSlowInEasing),
                targetOffsetX = { -it / 6 }
            ) + fadeOut(tween(NavigationAnimationDurationMillis))
        },
        popEnterTransition = {
            scaleIn(
                animationSpec = tween(NavigationAnimationDurationMillis, easing = FastOutSlowInEasing),
                initialScale = 0.92f
            ) + fadeIn(tween(NavigationAnimationDurationMillis))
        },
        popExitTransition = {
            slideOutHorizontally(
                animationSpec = tween(NavigationAnimationDurationMillis, easing = FastOutSlowInEasing),
                targetOffsetX = { it / 3 }
            ) + fadeOut(tween(NavigationAnimationDurationMillis))
        },
    ) {
        splashNavGraph(
            navController = appState.navController
        )

        homeNavGraph(
            paddingValues = paddingValues,
            navigateToCreateCourse = appState.navController::navigateToCreateCourse,
            navigateToDetailCourse = appState.navController::navigateToDetailCourse
        )

        courseNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues
        )

        signupNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues
        )

        loginNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues
        )

        exploreNavGraph(
            navController = appState.navController,
            paddingValues = paddingValues
        )

        favoriteNavGraph(
            paddingValues = paddingValues,
            navigateToExplore = { appState.navigateToTab(MainTab.EXPLORE) }
        )

        myPageNavGraph(
            paddingValues = paddingValues,
            navController = appState.navController,
            navigateUp = appState::navigateUp
        )
    }
}
