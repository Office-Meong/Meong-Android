package com.office.meong.presentation.sharedcomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun CourseEmptyContent(
    onClickPillButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MeongTheme.colors.white,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 34.dp)
            .padding(top = 34.dp, bottom = 27.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "아직 만든 코스가 없어요",
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray800
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "오피스멍에서 워케이션 코스를 만들어보세요!",
            style = MeongTheme.typography.body.body14M,
            color = MeongTheme.colors.gray500
        )

        Spacer(modifier = Modifier.height(18.dp))

        MeongPillButton(
            text = "맞춤 코스 만들기",
            isPrimary = true,
            onClick = onClickPillButton,
            suffixIcon = R.drawable.ic_chevron_right
        )
    }
}

@Preview
@Composable
private fun CourseEmptyContentPreview() {
    MeongTheme {
        CourseEmptyContent(
            onClickPillButton = {}
        )
    }
}
