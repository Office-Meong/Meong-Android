package com.office.meong.presentation.main.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.office.meong.core.model.trigger.RefreshState
import com.office.meong.core.network.monitor.NetworkMonitor
import com.office.meong.presentation.explore.navigation.navigateToExplore
import com.office.meong.presentation.course.my.navigation.navigateToMyCourse
import com.office.meong.presentation.favorite.navigation.navigateToFavorite
import com.office.meong.presentation.home.navigation.navigateToHome
import com.office.meong.presentation.main.MainTab
import com.office.meong.presentation.mypage.navigation.navigateToMyPage
import com.office.meong.presentation.splash.navigation.Splash
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
    val startDestination = Splash

    /** 현재 탭을 다시 눌렀을 때 화면에 알리는 신호. */
    val refreshState = RefreshState()

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
        // 이미 그 탭에 있으면 이동하지 않고 신호만 보낸다(화면 재생성·재조회 없이 맨 위로/새로고침).
        if (tab == currentTab.value) {
            refreshState.trigger()
            return
        }

        // 탭을 떠날 때 그 탭의 상태(스크롤·필터·페이징)를 저장하고, 돌아올 때 복원한다.
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

        when (tab) {
            MainTab.HOME -> navController.navigateToHome(navOptions = navOptions)
            MainTab.EXPLORE -> navController.navigateToExplore(navOptions = navOptions)
            MainTab.MY_COURSE -> navController.navigateToMyCourse(navOptions = navOptions)
            MainTab.FAVORITE -> navController.navigateToFavorite(navOptions = navOptions)
            MainTab.MY_PAGE -> navController.navigateToMyPage(navOptions = navOptions)
        }
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
): MainAppState = remember(networkMonitor, navController, coroutineScope) {
    MainAppState(
        networkMonitor = networkMonitor,
        navController = navController,
        coroutineScope = coroutineScope
    )
}
