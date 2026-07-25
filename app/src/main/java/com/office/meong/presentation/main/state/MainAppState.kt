package com.office.meong.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.office.meong.core.network.monitor.NetworkMonitor
import com.office.meong.presentation.explore.navigation.navigation.navigateToExplore
import com.office.meong.presentation.favorite.navigation.navigateToFavorite
import com.office.meong.presentation.home.navigation.Home
import com.office.meong.presentation.home.navigation.navigateToHome
import com.office.meong.presentation.main.MainTab
import com.office.meong.presentation.course.my.navigation.navigateToMyCourse
import com.office.meong.presentation.mypage.navigation.navigateToMyPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
class MainAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val startDestination = Home

    val isOffline: StateFlow<Boolean> = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val currentDestination = navController.currentBackStackEntryFlow
        .map { it.destination }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val currentTab: StateFlow<MainTab?> = currentDestination
        .map { destination ->
            MainTab.find { tab ->
                destination?.hasRoute(tab::class) == true
            }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val isBottomBarVisible: StateFlow<Boolean> = currentDestination
        .map { destination ->
            MainTab.contains { route -> destination?.hasRoute(route::class) == true }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun navigateToTab(tab: MainTab) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(it) {
                    inclusive = true
                    saveState = true
                }
                restoreState = true
                launchSingleTop = true
            }
        }

        // 매번 새로 생성 - Screen의 내용이 매번 갱신되어야 할 때
        val refreshNavOptions = navOptions {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions = navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore(navOptions = refreshNavOptions)
            MainTab.MY_COURSE -> navController.navigateToMyCourse(navOptions = navOptions)
            MainTab.FAVORITE -> navController.navigateToFavorite(navOptions = navOptions)
            MainTab.MY_PAGE -> navController.navigateToMyPage(navOptions = navOptions)
        }
    }

    private val clearStackNavOptions = navOptions {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberMainAppState(
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MainAppState {
    return remember(networkMonitor, navController, coroutineScope) {
        MainAppState(
            networkMonitor = networkMonitor,
            navController = navController,
            coroutineScope = coroutineScope
        )
    }
}
