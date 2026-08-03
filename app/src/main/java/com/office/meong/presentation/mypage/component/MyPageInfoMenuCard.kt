package com.office.meong.presentation.mypage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun MyPageInfoMenuCard(
    appVersion: String,
    onTermsOfServiceClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onOpenSourceLicenseClick: () -> Unit,
    onFeedbackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        MyPageSectionItem(
            itemName = "서비스 이용약관",
            onClick = onTermsOfServiceClick
        )

        MyPageSectionItem(
            itemName = "개인정보 처리방침",
            onClick = onPrivacyPolicyClick
        )

        MyPageSectionItem(
            itemName = "오픈소스 라이선스",
            onClick = onOpenSourceLicenseClick
        )

        MyPageSectionItem(
            itemName = "문의/피드백",
            onClick = onFeedbackClick
        )

        MyPageSectionItem(
            itemName = "현재 앱버전",
            trailingText = appVersion,
            showChevron = false
        )
    }
}

@Preview
@Composable
private fun MyPageInfoMenuCardPreview() {
    MeongTheme {
        MyPageInfoMenuCard(
            appVersion = "1.0.0",
            onTermsOfServiceClick = {},
            onPrivacyPolicyClick = {},
            onOpenSourceLicenseClick = {},
            onFeedbackClick = {}
        )
    }
}
