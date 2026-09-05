package com.office.meong.presentation.mypage.withdraw

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.office.meong.R
import com.office.meong.core.common.extension.collectSideEffect
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.button.MeongButton
import com.office.meong.core.designsystem.component.dialog.MeongDialog
import com.office.meong.core.designsystem.component.dialog.action.MeongCancelAction
import com.office.meong.core.designsystem.component.dialog.action.MeongConfirmAction
import com.office.meong.core.designsystem.component.topbar.MeongTopbar
import com.office.meong.core.designsystem.theme.MeongTheme
import com.office.meong.core.model.trigger.SnackbarState
import com.office.meong.core.trigger.LocalGlobalUiEventTrigger
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val withdrawNotices: ImmutableList<String> = persistentListOf(
    "탈퇴 시 현재 로그인된 계정은 즉시 탈퇴 처리됩니다.",
    "탈퇴 후에는 저장한 코스, 관심 장소, 반려견 정보를 다시 확인할 수 없습니다.",
    "삭제된 데이터는 복구할 수 없습니다.",
    "다시 이용하려면 신규 회원가입이 필요합니다.",
)

@Composable
fun MyPageWithdrawRoute(
    paddingValues: PaddingValues,
    onBackClick: () -> Unit = {},
    navigateToLogin: () -> Unit = {},
    viewModel: WithdrawViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val globalUiEventHolder = LocalGlobalUiEventTrigger.current

    viewModel.sideEffect.collectSideEffect {
        when (it) {
            is WithdrawSideEffect.NavigateToLogin -> navigateToLogin()
            is WithdrawSideEffect.ShowSnackBar -> {
                globalUiEventHolder.showSnackbar(SnackbarState(message = it.message))
            }
        }
    }

    MyPageWithdrawScreen(
        paddingValues = paddingValues,
        state = state,
        onBackClick = onBackClick,
        onAgreementChange = viewModel::onAgreementChange,
        onWithdrawConfirm = viewModel::onWithdrawConfirm
    )
}

@Composable
private fun MyPageWithdrawScreen(
    paddingValues: PaddingValues,
    state: WithdrawState,
    onBackClick: () -> Unit,
    onAgreementChange: (Boolean) -> Unit,
    onWithdrawConfirm: () -> Unit
) {
    var isConfirmDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeongTheme.colors.white)
            .padding(paddingValues)
    ) {
        MeongTopbar(
            title = "회원 탈퇴",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "탈퇴 시 유의사항",
                style = MeongTheme.typography.label.label14Sb,
                color = MeongTheme.colors.gray900
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                withdrawNotices.forEachIndexed { index, notice ->
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = "${index + 1}.",
                            style = MeongTheme.typography.body.body12M,
                            color = MeongTheme.colors.gray700
                        )

                        Spacer(modifier = Modifier.size(6.dp))

                        Text(
                            text = notice,
                            style = MeongTheme.typography.body.body12M,
                            color = MeongTheme.colors.gray700
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AgreementRow(
                isChecked = state.isAgreed,
                onCheckedChange = onAgreementChange
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        MeongButton(
            text = "회원 탈퇴",
            isEnabled = state.isWithdrawEnabled,
            containerColor = MeongTheme.colors.red,
            onClick = { isConfirmDialogVisible = true },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
        )
    }

    if (isConfirmDialogVisible) {
        MeongDialog(
            onDismiss = { isConfirmDialogVisible = false },
            title = "정말 탈퇴하시겠어요?",
            subDescription = "탈퇴하면 저장된 정보를 모두 잃게 돼요",
            cancelAction = MeongCancelAction(onClick = { isConfirmDialogVisible = false }),
            confirmAction = MeongConfirmAction(
                text = "탈퇴",
                backgroundColor = MeongTheme.colors.red,
                onClick = {
                    isConfirmDialogVisible = false
                    onWithdrawConfirm()
                }
            )
        )
    }
}

@Composable
private fun AgreementRow(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { onCheckedChange(!isChecked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkboxShape = RoundedCornerShape(4.dp)
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = if (isChecked) MeongTheme.colors.primary else MeongTheme.colors.white,
                    shape = checkboxShape
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) MeongTheme.colors.primary else MeongTheme.colors.gray300,
                    shape = checkboxShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                contentDescription = null,
                tint = MeongTheme.colors.white,
                modifier = Modifier.size(12.dp)
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = "위 내용을 확인하고 탈퇴에 동의합니다.",
            style = MeongTheme.typography.body.body12M,
            color = MeongTheme.colors.gray700
        )
    }
}

@Preview
@Composable
private fun MyPageWithdrawScreenPreview() {
    MeongTheme {
        MyPageWithdrawScreen(
            paddingValues = PaddingValues(),
            state = WithdrawState(isAgreed = true),
            onBackClick = {},
            onAgreementChange = {},
            onWithdrawConfirm = {}
        )
    }
}
