package com.office.meong.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.BuildConfig
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.presentation.mypage.action.MyPageAccountActions
import com.office.meong.presentation.mypage.action.MyPageActions
import com.office.meong.presentation.mypage.action.MyPageInfoActions
import com.office.meong.presentation.mypage.component.MyPageAccountMenuCard
import com.office.meong.presentation.mypage.component.MyPageInfoMenuCard
import com.office.meong.presentation.mypage.component.MyPageUserInfoHolder

@Composable
fun MyPageRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit = {},
    navigateToOpenSourceLicense: () -> Unit = {},
    navigateToFeedback: () -> Unit = {},
    navigateToTermsOfService: () -> Unit = {},
    navigateToPrivacyPolicy: () -> Unit = {},
    navigateToPetEdit: () -> Unit = {}
) {
    val actions = remember(
        navigateToOpenSourceLicense,
        navigateToFeedback,
        navigateToTermsOfService,
        navigateToPrivacyPolicy
    ) {
        object : MyPageActions {
            override val info = object : MyPageInfoActions {
                override fun onClickTermsOfService() = navigateToTermsOfService()
                override fun onClickPrivacyPolicy() = navigateToPrivacyPolicy()
                override fun onClickOpenSourceLicense() = navigateToOpenSourceLicense()
                override fun onClickFeedback() = navigateToFeedback()
            }
            override val account = object : MyPageAccountActions {
                override fun onClickLogout() {
                    // TODO: 로그아웃 처리
                }
                override fun onClickWithdraw() {
                    // TODO: 회원 탈퇴 처리
                }
            }
        }
    }

    MyPageScreen(
        paddingValues = paddingValues,
        actions = actions,
        navigateToPetEdit = navigateToPetEdit
    )
}

@Composable
private fun MyPageScreen(
    paddingValues: PaddingValues,
    actions: MyPageActions,
    navigateToPetEdit: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.gray50)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "마이페이지",
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(20.dp))

        MyPageUserInfoHolder(
            onPetInfoClick = navigateToPetEdit
        )

        Spacer(modifier = Modifier.height(10.dp))

        MyPageInfoMenuCard(
            appVersion = BuildConfig.VERSION_NAME,
            onTermsOfServiceClick = actions.info::onClickTermsOfService,
            onPrivacyPolicyClick = actions.info::onClickPrivacyPolicy,
            onOpenSourceLicenseClick = actions.info::onClickOpenSourceLicense,
            onFeedbackClick = actions.info::onClickFeedback
        )

        Spacer(modifier = Modifier.height(10.dp))

        MyPageAccountMenuCard(
            onLogoutClick = actions.account::onClickLogout,
            onWithdrawClick = actions.account::onClickWithdraw
        )

        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Preview
@Composable
private fun MyPageRoutePreview() {
    MeongTheme {
        MyPageScreen(
            paddingValues = PaddingValues(),
            actions = remember {
                object : MyPageActions {
                    override val info = object : MyPageInfoActions {
                        override fun onClickTermsOfService() {}
                        override fun onClickPrivacyPolicy() {}
                        override fun onClickOpenSourceLicense() {}
                        override fun onClickFeedback() {}
                    }
                    override val account = object : MyPageAccountActions {
                        override fun onClickLogout() {}
                        override fun onClickWithdraw() {}
                    }
                }
            }
        )
    }
}