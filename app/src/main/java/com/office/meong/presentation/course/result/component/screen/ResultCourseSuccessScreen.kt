package com.office.meong.presentation.course.result.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.component.button.MeongPillButton
import com.office.meong.core.designsystem.component.image.StableImage
import com.office.meong.core.designsystem.theme.MeongTheme

@Composable
fun ResultCourseSuccessScreen(
    navigateToDetailCourse: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MeongTheme.colors.white
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StableImage(
            drawableResId = R.drawable.img_character_complete,
            modifier = Modifier
                .size(130.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "워케이션 코스를 저장했어요!",
            style = MeongTheme.typography.title.title16Sb,
            color = MeongTheme.colors.gray800,
        )

        Spacer(modifier = Modifier.height(24.dp))

        MeongPillButton(
            text = "저장한 코스 보러가기",
            isPrimary = true,
            onClick = navigateToDetailCourse
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "홈으로 돌아가기",
            style = MeongTheme.typography.label.label12Sb,
            color = MeongTheme.colors.gray500,
            modifier = Modifier
                .minimumInteractiveComponentSize()
                .noRippleClickable(
                    onClick = navigateToHome
                )
        )
    }
}

@Preview
@Composable
private fun ResultCourseSuccessScreenPreview() {
    MeongTheme {
        ResultCourseSuccessScreen(
            navigateToDetailCourse = {},
            navigateToHome = {}
        )
    }
}
