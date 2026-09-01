package com.office.meong.presentation.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.office.meong.core.navigation.MainTabRoute
import com.office.meong.core.navigation.Route
import com.office.meong.presentation.auth.navigation.navigateToLogin
import com.office.meong.presentation.mypage.MyPageRoute
import com.office.meong.presentation.mypage.licenses.OssLicensesScreen
import com.office.meong.presentation.mypage.licenses.navigation.OssLicenses
import com.office.meong.presentation.mypage.licenses.navigation.navigateToOssLicenses
import com.office.meong.presentation.mypage.petedit.MyPagePetEditRoute
import com.office.meong.presentation.mypage.petedit.navigation.MyPagePetEdit
import com.office.meong.presentation.mypage.petedit.navigation.navigateToMyPagePetEdit
import com.office.meong.presentation.mypage.useredit.MyPageUserEditRoute
import com.office.meong.presentation.mypage.useredit.navigation.MyPageUserEdit
import com.office.meong.presentation.mypage.useredit.navigation.navigateToMyPageUserEdit
import com.office.meong.presentation.mypage.withdraw.MyPageWithdrawRoute
import com.office.meong.presentation.mypage.withdraw.navigation.MyPageWithdraw
import com.office.meong.presentation.mypage.withdraw.navigation.navigateToMyPageWithdraw
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
                navigateToPetEdit = navController::navigateToMyPagePetEdit,
                navigateToUserEdit = navController::navigateToMyPageUserEdit,
                navigateToWithdraw = navController::navigateToMyPageWithdraw,
                navigateToLogin = {
                    navController.navigateToLogin(
                        navOptions { popUpTo(0) { inclusive = true } }
                    )
                }
            )
        }

        composable<MyPageWithdraw> {
            MyPageWithdrawRoute(
                paddingValues = paddingValues,
                onBackClick = navigateUp,
                navigateToLogin = {
                    navController.navigateToLogin(
                        navOptions { popUpTo(0) { inclusive = true } }
                    )
                }
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

        composable<MyPageUserEdit> {
            MyPageUserEditRoute(
                paddingValues = paddingValues,
                onCloseClick = navigateUp
            )
        }
    }
}