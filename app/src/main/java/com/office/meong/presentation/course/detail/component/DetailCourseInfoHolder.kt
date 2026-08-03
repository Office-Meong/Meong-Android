package com.office.meong.presentation.course.detail.component

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

@Composable
fun DetailCourseInfoHolder(
    location: String,
    tripDay: String,
    onEditTitleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "강릉 2박 3일 워케이션",
                style = MeongTheme.typography.title.title20Sb,
                color = MeongTheme.colors.gray900
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_pencil),
                contentDescription = "수정하기",
                tint = MeongTheme.colors.gray900,
                modifier = Modifier
                    .size(30.dp)
                    .padding(7.dp)
                    .noRippleClickable(onClick = onEditTitleClick)
            )
        }

        Spacer(Modifier.height(4.dp))

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = location,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray600,
            )

            VerticalDivider(
                thickness = 1.dp,
                color = MeongTheme.colors.gray200,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 5.dp)
            )

            Text(
                text = tripDay,
                style = MeongTheme.typography.body.body14M,
                color = MeongTheme.colors.gray600,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailCourseInfoHolderPreview() {
    MeongTheme {
        DetailCourseInfoHolder(
            location = "강릉",
            tripDay = "2박 3일 (2026.8.10 - 2026.8.12)",
            onEditTitleClick = {}
        )
    }
}
