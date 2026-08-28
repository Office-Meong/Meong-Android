package com.office.meong.presentation.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.disableUpWardEvent
import com.office.meong.core.designsystem.theme.MeongTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsBottomSheet(
    isServiceTermAgreed: Boolean,
    isPrivacyTermAgreed: Boolean,
    isSignUpEnabled: Boolean,
    onServiceTermClick: () -> Unit,
    onPrivacyTermClick: () -> Unit,
    onViewServiceTermClick: () -> Unit,
    onViewPrivacyTermClick: () -> Unit,
    onDismiss: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = null
    ) {
        TermsBottomSheetContent(
            isServiceTermAgreed = isServiceTermAgreed,
            isPrivacyTermAgreed = isPrivacyTermAgreed,
            isSignUpEnabled = isSignUpEnabled,
            onServiceTermClick = onServiceTermClick,
            onPrivacyTermClick = onPrivacyTermClick,
            onViewServiceTermClick = onViewServiceTermClick,
            onViewPrivacyTermClick = onViewPrivacyTermClick,
            onDismiss = onDismiss,
            onSignUpClick = onSignUpClick
        )
    }
}
@Composable
private fun TermsBottomSheetContent(
    isServiceTermAgreed: Boolean,
    isPrivacyTermAgreed: Boolean,
    isSignUpEnabled: Boolean,
    onServiceTermClick: () -> Unit,
    onPrivacyTermClick: () -> Unit,
    onViewServiceTermClick: () -> Unit,
    onViewPrivacyTermClick: () -> Unit,
    onDismiss: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .disableUpWardEvent()
            .background(color = MeongTheme.colors.white)
            .padding(vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "회원가입",
                style = MeongTheme.typography.title.title16Sb,
                color = MeongTheme.colors.gray900
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                contentDescription = "닫기",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDismiss() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "아래 약관에 동의 후 서비스 이용이 가능해요.",
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray900,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TermItemRow(
            text = "(필수) 오피스멍 이용약관 동의",
            isChecked = isServiceTermAgreed,
            onClick = onServiceTermClick,
            onViewClick = onViewServiceTermClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        TermItemRow(
            text = "(필수) 개인정보 필수 동의",
            isChecked = isPrivacyTermAgreed,
            onClick = onPrivacyTermClick,
            onViewClick = onViewPrivacyTermClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSignUpClick,
            enabled = isSignUpEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MeongTheme.colors.primary,
                disabledContainerColor = MeongTheme.colors.gray200,
                contentColor = Color.White,
                disabledContentColor = MeongTheme.colors.gray500
            )
        ) {
            Text(
                text = "회원가입",
                style = MeongTheme.typography.label.label16Sb
            )
        }
    }
}

@Composable
private fun TermItemRow(
    text: String,
    isChecked: Boolean,
    onClick: () -> Unit,
    onViewClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = if (isChecked) MeongTheme.colors.primary else Color.Transparent,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) MeongTheme.colors.primary else MeongTheme.colors.gray300,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isChecked) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray700,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_chevron_right),
            contentDescription = "약관 보기",
            tint = MeongTheme.colors.gray500,
            modifier = Modifier
                .size(16.dp)
                .clickable { onViewClick() }
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun TermsBottomSheetPreview() {
    MeongTheme {
        TermsBottomSheetContent(
            isServiceTermAgreed = true,
            isPrivacyTermAgreed = false,
            isSignUpEnabled = false,
            onServiceTermClick = {},
            onPrivacyTermClick = {},
            onViewServiceTermClick = {},
            onViewPrivacyTermClick = {},
            onDismiss = {},
            onSignUpClick = {}
        )
    }
}
