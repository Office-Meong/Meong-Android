package com.office.meong.presentation.course.result.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.office.meong.R
import com.office.meong.core.common.extension.noRippleClickable
import com.office.meong.core.designsystem.theme.MeongTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ResultCourseInfoHolder(
    title: String,
    onEditTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.gray900
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_pencil),
                contentDescription = "수정하기",
                modifier = Modifier
                    .size(30.dp)
                    .padding(7.dp)
                    .noRippleClickable(onClick = onEditTitleClick),
                tint = MeongTheme.colors.gray900
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "입력 조건",
            style = MeongTheme.typography.label.label14Sb,
            color = MeongTheme.colors.gray900,
            modifier = Modifier
                .padding(vertical = 10.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        InfoSection(
            tripSummaryInfo = persistentListOf("강릉", "2박 3일 (2026.8.10 - 2026.8.12)", "몽몽이·소형견"),
            workStyleInfo = persistentListOf("업무 11:00-18:00", "펜션 선호", "업무 집중형")
        )
    }
}

@Composable
private fun InfoSection(
    tripSummaryInfo: ImmutableList<String>,
    workStyleInfo: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        InfoRow(
            items = tripSummaryInfo
        )
        InfoRow(
            items = workStyleInfo
        )
    }
}

@Composable
private fun InfoRow(
    items: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, text ->
            Text(
                text = text,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray600
            )

            if (index < items.lastIndex) {
                VerticalDivider(
                    thickness = 1.dp,
                    color = MeongTheme.colors.gray200,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 5.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ResultCourseInfoHolderPreview() {
    MeongTheme {
        ResultCourseInfoHolder(
            title = "강릉 2박 3일 워케이션",
            onEditTitleClick = {},
        )
    }
}
