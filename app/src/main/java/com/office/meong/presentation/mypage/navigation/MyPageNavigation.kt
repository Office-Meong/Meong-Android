package com.office.meong.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.mypage.MyPageRoute
import com.office.meong.presentation.mypage.licenses.OssLicensesScreen
import com.office.meong.presentation.mypage.licenses.navigation.OssLicenses
import com.office.meong.presentation.mypage.licenses.navigation.navigateToOssLicenses
import com.office.meong.presentation.mypage.petedit.MyPagePetEditRoute
import com.office.meong.presentation.mypage.petedit.navigation.MyPagePetEdit
import com.office.meong.presentation.mypage.petedit.navigation.navigateToMyPagePetEdit
import kotlinx.serialization.Serializable

@Serializable
data object MyPageGraph: Route

@Serializable
data object MyPage: MainTabRoute

fun NavController.navigateToMyPage(navOptions: NavOptions? = null) =
    navigate(MyPage, navOptions)

fun NavGraphBuilder.myPageNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateUp: () -> Unit,
) {
    navigation<MyPageGraph>(
        startDestination = MyPage
    ) {
        composable<MyPage> {
            MyPageRoute(
                paddingValues = paddingValues,
                navigateToOpenSourceLicense = navController::navigateToOssLicenses,
                navigateToPetEdit = navController::navigateToMyPagePetEdit
            )
        }

        composable<OssLicenses> {
            OssLicensesScreen(
                paddingValues = paddingValues,
                onBackClick = navigateUp
            )
        }

        composable<MyPagePetEdit> {
            MyPagePetEditRoute(
                paddingValues = paddingValues,
                onCloseClick = navigateUp
            )
        }
    }
}