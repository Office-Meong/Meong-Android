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
import androidx.compose.foundation.layout.width
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
import com.office.meong.presentation.course.detail.model.DetailCourseRouteIndicatorType

@Composable
fun DetailCourseScheduleSection(
    dayNumber: String,
    tripDay: String,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onRouteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_left),
                contentDescription = "이전",
                modifier = Modifier
                    .size(16.dp)
                    .noRippleClickable(onClick = onPreviousClick),
                tint = MeongTheme.colors.gray900
            )

            Spacer(Modifier.width(10.dp))

            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${dayNumber}일차",
                    style = MeongTheme.typography.label.label14Sb,
                    color = MeongTheme.colors.gray900
                )

                VerticalDivider(
                    Modifier
                        .fillMaxHeight()
                        .padding(vertical = 5.dp),
                    1.dp,
                    MeongTheme.colors.gray200
                )

                Text(
                    text = tripDay,
                    style = MeongTheme.typography.label.label14Sb,
                    color = MeongTheme.colors.gray700
                )
            }

            Spacer(Modifier.width(10.dp))

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
                contentDescription = "다음",
                Modifier
                    .size(16.dp)
                    .noRippleClickable(onClick = onNextClick),
                tint = MeongTheme.colors.gray900
            )
        }

        Spacer(Modifier.height(16.dp))

        DetailCoursePlaceSummaryItem(
            placeType = "숙소",
            placeName = "프렌즈애견펜션"
        )

        Spacer(Modifier.height(10.dp))

        DetailCourseRouteIndicator(
            routeLength = "1.2",
            onRouteClick = onRouteClick,
            routeIndicatorType = DetailCourseRouteIndicatorType.START
        )

        Spacer(Modifier.height(10.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailCourseScheduleSectionPreview() {
    MeongTheme {
        DetailCourseScheduleSection(
            dayNumber = "2",
            tripDay = "8.11",
            onPreviousClick = {},
            onNextClick = {},
            onRouteClick = {}
        )
    }
}
