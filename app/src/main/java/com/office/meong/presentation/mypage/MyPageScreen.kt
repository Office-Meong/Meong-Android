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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.BuildConfig
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.openUrl
import com.office.meong.core.common.util.UiState
import com.office.meong.core.common.util.successData
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongCancelAction
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.pet.PetInfo
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.data.user.model.UserInfoModel
import com.office.meong.presentation.mypage.action.MyPageAccountActions
import com.office.meong.presentation.mypage.action.MyPageActions
import com.office.meong.presentation.mypage.action.MyPageInfoActions
import com.office.meong.presentation.mypage.component.MyPageAccountMenuCard
import com.office.meong.presentation.mypage.component.MyPageInfoMenuCard
import com.office.meong.presentation.mypage.component.MyPageUserInfoHolder
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MyPageRoute(
    paddingValues: PaddingValues,
    navigateToOpenSourceLicense: () -> Unit = {},
    navigateToPetEdit: () -> Unit = {},
    navigateToUserEdit: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    viewModel: MyPageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    var isLogoutDialogVisible by remember { mutableStateOf(false) }
    var isWithdrawDialogVisible by remember { mutableStateOf(false) }

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is MyPageSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
            is MyPageSideEffect.NavigateToLogin -> navigateToLogin()
        }
    }

    val actions = remember(state.policy) {
        val policy = state.policy.successData

        object : MyPageActions {
            override val info = object : MyPageInfoActions {
                override fun onClickTermsOfService() {
                    policy?.termsUrl?.let(context::openUrl)
                }
                override fun onClickPrivacyPolicy() {
                    policy?.privacyUrl?.let(context::openUrl)
                }
                override fun onClickOpenSourceLicense() = navigateToOpenSourceLicense()
                override fun onClickFeedback() {
                    policy?.inquiryUrl?.let(context::openUrl)
                }
            }
            override val account = object : MyPageAccountActions {
                override fun onClickLogout() {
                    isLogoutDialogVisible = true
                }
                override fun onClickWithdraw() {
                    isWithdrawDialogVisible = true
                }
            }
        }
    }

    MyPageScreen(
        paddingValues = paddingValues,
        userInfo = state.userInfo,
        petInfo = state.petInfo,
        actions = actions,
        navigateToPetEdit = navigateToPetEdit,
        navigateToUserEdit = navigateToUserEdit
    )

    if (isLogoutDialogVisible) {
        MeongDialog(
            onDismiss = { isLogoutDialogVisible = false },
            title = "로그아웃 하시겠어요?",
            cancelAction = MeongCancelAction(onClick = { isLogoutDialogVisible = false }),
            confirmAction = MeongConfirmAction(
                text = "로그아웃",
                onClick = {
                    isLogoutDialogVisible = false
                    viewModel.onLogoutClick()
                }
            )
        )
    }

    if (isWithdrawDialogVisible) {
        MeongDialog(
            onDismiss = { isWithdrawDialogVisible = false },
            title = "정말 탈퇴하시겠어요?",
            subDescription = "탈퇴하면 저장된 정보를 모두 잃게 돼요",
            cancelAction = MeongCancelAction(onClick = { isWithdrawDialogVisible = false }),
            confirmAction = MeongConfirmAction(
                text = "탈퇴",
                backgroundColor = MeongTheme.colors.red,
                onClick = {
                    isWithdrawDialogVisible = false
                    viewModel.onWithdrawClick()
                }
            )
        )
    }
}

@Composable
private fun MyPageScreen(
    paddingValues: PaddingValues,
    userInfo: UiState<UserInfoModel>,
    petInfo: UiState<PetInfo>,
    actions: MyPageActions,
    navigateToPetEdit: () -> Unit = {},
    navigateToUserEdit: () -> Unit = {}
) {
    val user = userInfo.successData
    val pet = petInfo.successData

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
            userNickname = user?.nickname ?: "",
            userEmail = user?.email ?: "",
            userProfileImageUrl = user?.profileImageUrl?.ifBlank { null },
            petName = pet?.name ?: "반려견을 등록해주세요",
            petTags = pet?.let {
                persistentListOf(
                    it.sizeCategory.label,
                    "활동량 ${it.activityLevel.label}",
                    "사회성 ${it.sociability.label}"
                )
            } ?: persistentListOf(),
            petProfileImageUrl = pet?.imageUrl?.ifBlank { null },
            onUserInfoClick = navigateToUserEdit,
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
            userInfo = UiState.Empty,
            petInfo = UiState.Empty,
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
