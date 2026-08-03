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
fun MyPageAccountMenuCard(
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
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
            itemName = "로그아웃",
            onClick = onLogoutClick
        )

        MyPageSectionItem(
            itemName = "회원 탈퇴",
            onClick = onWithdrawClick
        )
    }
}

@Preview
@Composable
private fun MyPageAccountMenuCardPreview() {
    MeongTheme {
        MyPageAccountMenuCard(
            onLogoutClick = {},
            onWithdrawClick = {}
        )
    }
}