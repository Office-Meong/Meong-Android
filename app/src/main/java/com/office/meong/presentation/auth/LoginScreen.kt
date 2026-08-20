package com.office.meong.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.openUrl
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import com.office.meong.presentation.auth.component.TermsBottomSheet
import com.office.meong.presentation.auth.model.LoginSideEffect
import com.office.meong.presentation.auth.model.LoginUiState

@Composable
fun LoginRoute(
    paddingValues: PaddingValues,
    navigateToHome: () -> Unit = {},
    navigateToSignup: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current
    val context = LocalContext.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is LoginSideEffect.NavigateToHome -> navigateToHome()
            is LoginSideEffect.NavigateToSignup -> navigateToSignup()
            is LoginSideEffect.ShowToast -> globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            is LoginSideEffect.OpenUrl -> context.openUrl(it.url)
        }
    }

    LoginScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onKakaoLoginClick = { viewModel.onKakaoLoginClick(context) },
        onServiceTermClick = viewModel::onServiceTermClick,
        onPrivacyTermClick = viewModel::onPrivacyTermClick,
        onViewServiceTermClick = viewModel::onViewServiceTermClick,
        onViewPrivacyTermClick = viewModel::onViewPrivacyTermClick,
        onBottomSheetDismiss = viewModel::onBottomSheetDismiss,
        onSignUpComplete = viewModel::onSignUpClick
    )
}

@Composable
private fun LoginScreen(
    paddingValues: PaddingValues,
    uiState: LoginUiState,
    onKakaoLoginClick: () -> Unit,
    onServiceTermClick: () -> Unit,
    onPrivacyTermClick: () -> Unit,
    onViewServiceTermClick: () -> Unit,
    onViewPrivacyTermClick: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onSignUpComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MeongTheme.colors.primaryBg,
                        MeongTheme.colors.white
                    )
                )
            )
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_top_logo),
                contentDescription = "오피스멍 로고",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_login_title),
                contentDescription = "우리 집 강아지와 나의 업무 스타일에 맞는 워케이션 코스 만들기",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_login),
                    contentDescription = "워케이션 멍멍이 캐릭터",
                    modifier = Modifier.fillMaxWidth(0.8f),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(color = Color(0xFFFAE100), shape = RoundedCornerShape(8.dp))
                    .clickable(onClick = onKakaoLoginClick),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_kakao),
                    contentDescription = "카카오 로고",
                    tint = Color.Black,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "카카오 계정으로 로그인",
                    style = MeongTheme.typography.label.label16Sb,
                    color = Color.Black
                )
            }
        }
    }

    if (uiState.isTermsBottomSheetVisible) {
        TermsBottomSheet(
            isServiceTermAgreed = uiState.isServiceTermAgreed,
            isPrivacyTermAgreed = uiState.isPrivacyTermAgreed,
            isSignUpEnabled = uiState.isSignUpEnabled,
            onServiceTermClick = onServiceTermClick,
            onPrivacyTermClick = onPrivacyTermClick,
            onViewServiceTermClick = onViewServiceTermClick,
            onViewPrivacyTermClick = onViewPrivacyTermClick,
            onDismiss = onBottomSheetDismiss,
            onSignUpClick = onSignUpComplete
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MeongTheme {
        LoginScreen(
            paddingValues = PaddingValues(),
            uiState = LoginUiState(isTermsBottomSheetVisible = true, isServiceTermAgreed = true),
            onKakaoLoginClick = {},
            onServiceTermClick = {},
            onPrivacyTermClick = {},
            onViewServiceTermClick = {},
            onViewPrivacyTermClick = {},
            onBottomSheetDismiss = {},
            onSignUpComplete = {}
        )
    }
}
