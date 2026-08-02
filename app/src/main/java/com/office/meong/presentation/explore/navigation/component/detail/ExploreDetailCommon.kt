package com.office.meong.presentation.explore.navigation.component.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ExploreDetailSectionContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray700
        )
        Spacer(modifier = Modifier.height(12.dp))

        content()
    }
}

@Composable
fun ExploreDetailInfoItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray500
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray900
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreDetailCommonPreview() {
    MeongTheme {
        ExploreDetailSectionContainer(title = "섹션 타이틀", modifier = Modifier.padding(16.dp)) {
            ExploreDetailInfoItem(title = "정보 제목", content = "정보 내용입니다.")
        }
    }
}