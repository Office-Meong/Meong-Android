package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ResultCourseTopSection(
    petName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MeongTheme.colors.white
            )
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(
                    MeongTheme.typography.title.title20Sb.copy(
                        color = MeongTheme.colors.primary
                    ).toSpanStyle()
                ) {
                    append(petName)
                }

                append("와 함께할\n" +
                        "워케이션 코스가 완성됐어요")
            },
            style = MeongTheme.typography.title.title20Sb,
            color = MeongTheme.colors.gray900,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "입력한 조건에 딱 맞는 코스를 생성했어요\n" +
                    "장소와 순서는 언제든 자유롭게 수정할 수 있어요",
            style = MeongTheme.typography.body.body12M,
            color = MeongTheme.colors.gray700
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview
@Composable
private fun ResultCourseTopSectionPreview() {
    MeongTheme {
        ResultCourseTopSection(petName = "몽몽이")
    }
}
